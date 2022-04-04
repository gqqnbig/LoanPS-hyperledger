package services.impl;

import com.owlike.genson.Genson;
import entities.*;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;

@Contract
public class TestHelper implements ContractInterface {
	private static final Genson genson = new Genson();

	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public void fixEmptyRequestedCAHistory(final Context ctx) {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		CheckingAccount account = new CheckingAccount();
		EntityManager.addCheckingAccountObject(account);


		getCurrentLoanRequest().setRequestedCAHistory(account);
		EntityManager.saveModified(LoanRequest.class);
	}

	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public void fixEmptyRequestedCreditHistory(final Context ctx) {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		CreditHistory creditHistory = new CreditHistory();
		EntityManager.addCreditHistoryObject(creditHistory);


		getCurrentLoanRequest().setRequestedCreditHistory(creditHistory);
		EntityManager.saveModified(LoanRequest.class);
	}

	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public void createLoanAccount(final Context ctx, int id, float balance, String status) {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var loanAccount = EntityManager.createLoanAccountObject();
		loanAccount.setLoanAccountID(id);
		loanAccount.setBalance(balance);
		loanAccount.setStatus(genson.deserialize(status, LoanAccountStatus.class));

		EntityManager.addLoanAccountObject(loanAccount);
		
	}

	private Object currentLoanRequestPK;
	private LoanRequest currentLoanRequest;

	public LoanRequest getCurrentLoanRequest() {
		return EntityManager.getLoanRequestByPK(getCurrentLoanRequestPK());
	}

	private Object getCurrentLoanRequestPK() {
		if (currentLoanRequestPK == null)
			currentLoanRequestPK = genson.deserialize(EntityManager.stub.getStringState("SubmitLoanRequestModuleImpl.currentLoanRequestPK"), Integer.class);

		return currentLoanRequestPK;
	}
}
