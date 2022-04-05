#!/bin/bash

oneTimeSetUp() {
	export FABRIC_CFG_PATH=$PWD/../config/

	if [[ -z "$GITHUB_WORKSPACE" ]]; then
		GITHUB_WORKSPACE=~/LoanPS-hyperledger/
	fi

	source $GITHUB_WORKSPACE/src/test/shell/as-org1.sh

	rm -f $GITHUB_WORKSPACE/loanps.tar.gz
	rm -rf "$GITHUB_WORKSPACE/build/install/"
	pushd "$GITHUB_WORKSPACE"
	./gradlew installDist
	popd
	peer lifecycle chaincode package $GITHUB_WORKSPACE/loanps.tar.gz --path $GITHUB_WORKSPACE/build/install/loanps --lang java --label loanps_1.0

}

setUp() {
	./network.sh down >/dev/null
	./network.sh up createChannel >/dev/null

	# start a subshell due to export variables.
	(
		export CORE_PEER_TLS_ENABLED=true
		export CORE_PEER_LOCALMSPID="Org1MSP"
		export CORE_PEER_TLS_ROOTCERT_FILE=${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
		export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp
		export CORE_PEER_ADDRESS=localhost:7051
		# The package ID is the combination of the chaincode label and a hash of the chaincode binaries. Every peer will generate the same package ID.
		packageId=$(peer lifecycle chaincode install $GITHUB_WORKSPACE/loanps.tar.gz 2>&1 | grep -o -P '(?<=identifier:\s).+:[\da-f]+$')
		if [[ -z "$packageId" ]]; then
			fail "Failed to install chaincode."
			return
		fi
		peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --channelID mychannel --name loanps --version 1.0 --package-id $packageId --sequence 1 --tls --cafile ${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem

		export CORE_PEER_LOCALMSPID="Org2MSP"
		export CORE_PEER_TLS_ROOTCERT_FILE=${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
		export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp
		export CORE_PEER_ADDRESS=localhost:9051
		peer lifecycle chaincode install $GITHUB_WORKSPACE/loanps.tar.gz

		peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --channelID mychannel --name loanps --version 1.0 --package-id $packageId --sequence 1 --tls --cafile ${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem

		peer lifecycle chaincode commit -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --channelID mychannel --name loanps --version 1.0 --sequence 1 --tls --cafile ${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem --peerAddresses localhost:7051 --tlsRootCertFiles ${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses localhost:9051 --tlsRootCertFiles ${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
	)
}

getBlockInfo() {
	peer channel fetch newest mychannel.block -c mychannel -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile ${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem
	configtxlator proto_decode --type common.Block --input mychannel.block
}

testManageLoanTerm() {
	pci -C mychannel -n loanps --waitForEvent -c '{"function":"ManageLoanTermCRUDServiceImpl:createLoanTerm","Args":["12","first"]}' || fail || return
	docker stop "$(docker ps -n 1 --filter 'name=dev' --format '{{.ID}}')"

	if pci -C mychannel -n loanps --waitForEvent -c '{"function":"ManageLoanTermCRUDServiceImpl:createLoanTerm","Args":["12","first"]}'; then
		fail || return
	fi

	output=$(peer chaincode query -C mychannel -n loanps -c '{"function":"EvaluateLoanRequestModuleImpl:listAvaiableLoanTerm","Args":[]}')
	assertNotEquals "[]" "$output"

	output=$(peer chaincode query -C mychannel -n loanps -c '{"function":"ManageLoanTermCRUDServiceImpl:queryLoanTerm","Args":["12"]}')
	assertContains "$output" "first"

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"ManageLoanTermCRUDServiceImpl:modifyLoanTerm","Args":["12","second"]}' || fail || return
	docker stop "$(docker ps -n 1 --filter 'name=dev' --format '{{.ID}}')"

	output=$(peer chaincode query -C mychannel -n loanps -c '{"function":"ManageLoanTermCRUDServiceImpl:queryLoanTerm","Args":["12"]}')
	assertContains "$output" "second"

	docker stop "$(docker ps -n 1 --filter 'name=dev' --format '{{.ID}}')"
	pci -C mychannel -n loanps --waitForEvent -c '{"function":"ManageLoanTermCRUDServiceImpl:deleteLoanTerm","Args":["12"]}' || fail || return

	if pci -C mychannel -n loanps --waitForEvent -c '{"function":"ManageLoanTermCRUDServiceImpl:deleteLoanTerm","Args":["12"]}'; then
		fail || return
	fi
}

testSubmitLoanRequest() {
	if pci -C mychannel -n loanps --waitForEvent -c '{"function":"EnterValidatedCreditReferencesModuleImpl:listSubmitedLoanRequest","Args":[]}'; then
		fail "When there are not loans, this method is expected to fail rather than returning empty array." || return
	fi

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"SubmitLoanRequestModuleImpl:enterLoanInformation","Args":["1","1","1","1","1","1","1","1","1","1","1","1","1"]}' || fail || return
	pci -C mychannel -n loanps --waitForEvent -c '{"function":"SubmitLoanRequestModuleImpl:creditRequest","Args":[]}' || fail || return
	pci -C mychannel -n loanps --waitForEvent -c '{"function":"SubmitLoanRequestModuleImpl:accountStatusRequest","Args":[]}' || fail || return

	writes=$(getBlockInfo | jq -r '.. |.ns_rwset? | .[]? | select(.namespace=="loanps"?)  | .rwset.writes[] | select(.key=="CheckingAccount"?).value' | base64 --decode)
	if [[ "$writes" != "[]" ]] && [[ "$writes" != "[null]" ]]; then
		fail "The default implementation of ThirdPartyServicesImpl returns null for getCheckingAccountStatus." || return
	fi

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"TestHelper:fixEmptyRequestedCAHistory","Args":[]}' || fail || return
	pci -C mychannel -n loanps --waitForEvent -c '{"function":"TestHelper:fixEmptyRequestedCreditHistory","Args":[]}' || fail || return

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"SubmitLoanRequestModuleImpl:calculateScore","Args":[]}' || fail || return

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"EnterValidatedCreditReferencesModuleImpl:listSubmitedLoanRequest","Args":[]}' || fail || return

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"EnterValidatedCreditReferencesModuleImpl:chooseLoanRequest","Args":["1"]}' || fail || return
	pci -C mychannel -n loanps --waitForEvent -c '{"function":"EnterValidatedCreditReferencesModuleImpl:markRequestValid","Args":[]}' || fail || return

	output=$(pci -C mychannel -n loanps --waitForEvent -c '{"function":"EvaluateLoanRequestModuleImpl:listTenLoanRequest","Args":[]}' 2>&1 |
		sed -n -r 's/.+status:200[[:space:]]+payload:"(.+)"[[:space:]]*$/\1/p')
	assertNotEquals "[]" "$output"
	pci -C mychannel -n loanps --waitForEvent -c '{"function":"EvaluateLoanRequestModuleImpl:chooseOneForReview","Args":["1"]}' || fail || return

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"EvaluateLoanRequestModuleImpl:checkCreditHistory","Args":[]}' || fail || return

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"EvaluateLoanRequestModuleImpl:reviewCheckingAccount","Args":[]}' || fail || return

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"ManageLoanTermCRUDServiceImpl:createLoanTerm","Args":["12","first"]}' || fail || return

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"EvaluateLoanRequestModuleImpl:addLoanTerm","Args":["12"]}' || fail || return

	output=$(pci -C mychannel -n loanps --waitForEvent -c '{"function":"GenerateLoanLetterAndAgreementModuleImpl:listApprovalRequest","Args":[]}' 2>&1 |
		sed -n -r 's/.+status:200[[:space:]]+payload:"(.+)"[[:space:]]*$/\1/p')
	assertEquals "[]" "$output"

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"EvaluateLoanRequestModuleImpl:approveLoanRequest","Args":[]}' || fail || return

	output=$(pci -C mychannel -n loanps --waitForEvent -c '{"function":"GenerateLoanLetterAndAgreementModuleImpl:listApprovalRequest","Args":[]}' 2>&1 |
		sed -n -r 's/.+status:200[[:space:]]+payload:"(.+)"[[:space:]]*$/\1/p')
	assertNotEquals "[]" "$output"

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"GenerateLoanLetterAndAgreementModuleImpl:genereateApprovalLetter","Args":["1"]}' || fail || return

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"GenerateLoanLetterAndAgreementModuleImpl:emailToAppliant","Args":[]}' || fail || return

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"GenerateLoanLetterAndAgreementModuleImpl:generateLoanAgreement","Args":[]}' || fail || return

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"GenerateLoanLetterAndAgreementModuleImpl:printLoanAgreement","Args":["1"]}' || fail || return
}

testBookNewLoan() {
	pci -C mychannel -n loanps --waitForEvent -c '{"function":"TestHelper:createLoanAccount","Args":["1000","0","NORMAL"]}' || fail || return
	if pci -C mychannel -n loanps --waitForEvent -c '{"function":"LoanProcessingSystemSystemImpl:bookNewLoan","Args":["1","1", "1000","2000-01-01","2020-01-01", "1"]}'; then
		fail || return
	fi

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"SubmitLoanRequestModuleImpl:enterLoanInformation","Args":["1","1","333","1","1","1","1","1","1","1","1","1","1"]}'

	pci -C mychannel -n loanps --waitForEvent -c '{"function":"LoanProcessingSystemSystemImpl:bookNewLoan","Args":["1","1", "1000","2000-01-01","2020-01-01", "1"]}' || fail || return

	peer chaincode query -C mychannel -n loanps -c '{"function":"LoanProcessingSystemSystemImpl:generateStandardPaymentNotice","Args":[]}' || fail || return
}

testScheduler() {
	peer chaincode query -C mychannel -n loanps -c '{"function":"LoanProcessingSystemSystemImpl:generateStandardPaymentNotice","Args":[]}' || fail || return
	peer chaincode query -C mychannel -n loanps -c '{"function":"LoanProcessingSystemSystemImpl:generateLateNotice","Args":[]}' || fail || return
}

source shunit2
