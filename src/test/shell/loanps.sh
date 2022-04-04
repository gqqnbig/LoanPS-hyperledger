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

testSubmitLoanRequest() {
	pci -C mychannel -n loanps --waitForEvent -c '{"function":"SubmitLoanRequestModuleImpl:enterLoanInformation","Args":["1","1","1","1","1","1","1","1","1","1","1","1","1"]}' || fail || return
	pci -C mychannel -n loanps --waitForEvent -c '{"function":"SubmitLoanRequestModuleImpl:creditRequest","Args":[]}' || fail || return
	pci -C mychannel -n loanps --waitForEvent -c '{"function":"SubmitLoanRequestModuleImpl:accountStatusRequest","Args":[]}' || fail || return
	pci -C mychannel -n loanps --waitForEvent -c '{"function":"SubmitLoanRequestModuleImpl:calculateScore","Args":[]}' || fail || return

}

source shunit2
