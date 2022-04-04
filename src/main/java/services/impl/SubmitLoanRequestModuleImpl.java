package services.impl;

import services.*;
import entities.*;
import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.Arrays;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import org.apache.commons.lang3.SerializationUtils;
import java.util.Iterator;
import org.hyperledger.fabric.shim.*;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.contract.*;
import com.owlike.genson.Genson;

@Contract
public class SubmitLoanRequestModuleImpl implements SubmitLoanRequestModule, Serializable, ContractInterface {
	private static final Genson genson = new Genson();
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public SubmitLoanRequestModuleImpl() {
		services = new ThirdPartyServicesImpl();
	}

	
	//Shared variable from system services
	
	/* Shared variable from system services and get()/set() methods */
			
	/* all get and set functions for temp property*/
				
	
	
	/* Generate inject for sharing temp variables between use cases in system service */
	
	
	/* Generate buiness logic according to functional requirement */
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean enterLoanInformation(final Context ctx, int requestid, String name, float loanamount, String loanpurpose, float income, int phonenumber, String postaladdress, int zipcode, String email, String workreferences, String creditreferences, int checkingaccountnumber, int securitynumber) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = enterLoanInformation(requestid, name, loanamount, loanpurpose, income, phonenumber, postaladdress, zipcode, email, workreferences, creditreferences, checkingaccountnumber, securitynumber);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean enterLoanInformation(int requestid, String name, float loanamount, String loanpurpose, float income, int phonenumber, String postaladdress, int zipcode, String email, String workreferences, String creditreferences, int checkingaccountnumber, int securitynumber) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get loanrequest
		LoanRequest loanrequest = null;
		//no nested iterator --  iterator: any previous:any
		for (LoanRequest loa : (List<LoanRequest>)EntityManager.getAllInstancesOf(LoanRequest.class))
		{
			if (loa.getRequestID() == requestid)
			{
				loanrequest = loa;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(loanrequest) == true) 
		{ 
			/* Logic here */
			LoanRequest loa = null;
			loa = (LoanRequest) EntityManager.createObject("LoanRequest");
			loa.setRequestID(requestid);
			loa.setName(name);
			loa.setLoanAmount(loanamount);
			loa.setLoanPurpose(loanpurpose);
			loa.setIncome(income);
			loa.setPhoneNumber(phonenumber);
			loa.setPostalAddress(postaladdress);
			loa.setZipCode(zipcode);
			loa.setEmail(email);
			loa.setWorkReferences(workreferences);
			loa.setCreditReferences(creditreferences);
			loa.setCheckingAccountNumber(checkingaccountnumber);
			loa.setSecurityNumber(securitynumber);
			EntityManager.addObject("LoanRequest", loa);
			this.setCurrentLoanRequest(loa);
			
			
			;
			// post-condition checking
			if (!(true && 
			loa.getRequestID() == requestid
			 && 
			loa.getName() == name
			 && 
			loa.getLoanAmount() == loanamount
			 && 
			loa.getLoanPurpose() == loanpurpose
			 && 
			loa.getIncome() == income
			 && 
			loa.getPhoneNumber() == phonenumber
			 && 
			loa.getPostalAddress() == postaladdress
			 && 
			loa.getZipCode() == zipcode
			 && 
			loa.getEmail() == email
			 && 
			loa.getWorkReferences() == workreferences
			 && 
			loa.getCreditReferences() == creditreferences
			 && 
			loa.getCheckingAccountNumber() == checkingaccountnumber
			 && 
			loa.getSecurityNumber() == securitynumber
			 && 
			StandardOPs.includes(((List<LoanRequest>)EntityManager.getAllInstancesOf(LoanRequest.class)), loa)
			 && 
			this.getCurrentLoanRequest() == loa
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			;				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//string parameters: [name, loanpurpose, postaladdress, email, workreferences, creditreferences]
		//all relevant vars : this loa
		//all relevant entities :  LoanRequest
	} 
	 
	static {opINVRelatedEntity.put("enterLoanInformation", Arrays.asList("","LoanRequest"));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean creditRequest(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = creditRequest();
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean creditRequest() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(this.getCurrentLoanRequest()) == false) 
		{ 
			/* Logic here */
			CreditHistory his = null;
			his = (CreditHistory) EntityManager.createObject("CreditHistory");
			his = services.getCreditHistory(getCurrentLoanRequest().getSecurityNumber(), getCurrentLoanRequest().getName());
			getCurrentLoanRequest().setRequestedCreditHistory(his);
			EntityManager.addObject("CreditHistory", his);
			
			
			;
			// post-condition checking
			if (!(true && 
			true
			 && 
			getCurrentLoanRequest().getRequestedCreditHistory() == his
			 && 
			StandardOPs.includes(((List<CreditHistory>)EntityManager.getAllInstancesOf(CreditHistory.class)), his)
			 && 
			EntityManager.saveModified(LoanRequest.class)
			 &&
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			;				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : his CurrentLoanRequest
		//all relevant entities : CreditHistory 
	} 
	 
	static {opINVRelatedEntity.put("creditRequest", Arrays.asList("CreditHistory",""));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean accountStatusRequest(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = accountStatusRequest();
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean accountStatusRequest() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(this.getCurrentLoanRequest()) == false) 
		{ 
			/* Logic here */
			CheckingAccount ca = null;
			ca = (CheckingAccount) EntityManager.createObject("CheckingAccount");
			ca = services.getCheckingAccountStatus(this.getCurrentLoanRequest().getCheckingAccountNumber());
			this.getCurrentLoanRequest().setRequestedCAHistory(ca);
			EntityManager.addObject("CheckingAccount", ca);
			
			
			;
			// post-condition checking
			if (!(true && 
			true
			 && 
			this.getCurrentLoanRequest().getRequestedCAHistory() == ca
			 && 
			StandardOPs.includes(((List<CheckingAccount>)EntityManager.getAllInstancesOf(CheckingAccount.class)), ca)
			 &&
			EntityManager.saveModified(LoanRequest.class)
			 &&
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			;				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : this ca
		//all relevant entities :  CheckingAccount
	} 
	 
	static {opINVRelatedEntity.put("accountStatusRequest", Arrays.asList("","CheckingAccount"));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public int calculateScore(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = calculateScore();
		return res;
	}

	@SuppressWarnings("unchecked")
	public int calculateScore() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(this.getCurrentLoanRequest()) == false && StandardOPs.oclIsundefined(getCurrentLoanRequest().getRequestedCAHistory()) == false && StandardOPs.oclIsundefined(getCurrentLoanRequest().getRequestedCreditHistory()) == false)
		{ 
			/* Logic here */
			this.getCurrentLoanRequest().setCreditScore(100);
			this.getCurrentLoanRequest().setStatus(LoanRequestStatus.SUBMITTED);
			
			
			;
			// post-condition checking
			if (!(this.getCurrentLoanRequest().getCreditScore() == 100
			 && 
			this.getCurrentLoanRequest().getStatus() == LoanRequestStatus.SUBMITTED
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			;				
			return this.getCurrentLoanRequest().getCreditScore();
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : this
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("calculateScore", Arrays.asList(""));}
	
	
	
	
	/* temp property for controller */
	private Object currentLoanRequestPK;
	private LoanRequest currentLoanRequest;
			
	/* all get and set functions for temp property*/
	public LoanRequest getCurrentLoanRequest() {
		return EntityManager.getLoanRequestByPK(getCurrentLoanRequestPK());
	}

	private Object getCurrentLoanRequestPK() {
		if (currentLoanRequestPK == null)
			currentLoanRequestPK = genson.deserialize(EntityManager.stub.getStringState("SubmitLoanRequestModuleImpl.currentLoanRequestPK"), Integer.class);

		return currentLoanRequestPK;
	}	
	
	public void setCurrentLoanRequest(LoanRequest currentloanrequest) {
		if (currentloanrequest != null)
			setCurrentLoanRequestPK(currentloanrequest.getPK());
		else
			setCurrentLoanRequestPK(null);
		this.currentLoanRequest = currentloanrequest;
	}

	private void setCurrentLoanRequestPK(Object currentLoanRequestPK) {
		String json = genson.serialize(currentLoanRequestPK);
		EntityManager.stub.putStringState("SubmitLoanRequestModuleImpl.currentLoanRequestPK", json);
		//If we set currentLoanRequestPK to null, the getter thinks this fields is not initialized, thus will read the old value from chain.
		if (currentLoanRequestPK != null)
			this.currentLoanRequestPK = currentLoanRequestPK;
		else
			this.currentLoanRequestPK = EntityManager.getGuid();
	}
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
