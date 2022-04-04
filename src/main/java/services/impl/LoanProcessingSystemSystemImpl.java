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
import converters.*;
import com.owlike.genson.GensonBuilder;

@Contract
public class LoanProcessingSystemSystemImpl implements LoanProcessingSystemSystem, Serializable, ContractInterface {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public LoanProcessingSystemSystemImpl() {
		services = new ThirdPartyServicesImpl();
	}

	public void refresh() {
		SubmitLoanRequestModule submitloanrequestmodule_service = (SubmitLoanRequestModule) ServiceManager
				.getAllInstancesOf(SubmitLoanRequestModule.class).get(0);
		EnterValidatedCreditReferencesModule entervalidatedcreditreferencesmodule_service = (EnterValidatedCreditReferencesModule) ServiceManager
				.getAllInstancesOf(EnterValidatedCreditReferencesModule.class).get(0);
		EvaluateLoanRequestModule evaluateloanrequestmodule_service = (EvaluateLoanRequestModule) ServiceManager
				.getAllInstancesOf(EvaluateLoanRequestModule.class).get(0);
		GenerateLoanLetterAndAgreementModule generateloanletterandagreementmodule_service = (GenerateLoanLetterAndAgreementModule) ServiceManager
				.getAllInstancesOf(GenerateLoanLetterAndAgreementModule.class).get(0);
		ManageLoanTermCRUDService manageloantermcrudservice_service = (ManageLoanTermCRUDService) ServiceManager
				.getAllInstancesOf(ManageLoanTermCRUDService.class).get(0);
	}			
	
	/* Generate buiness logic according to functional requirement */
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean bookNewLoan(final Context ctx, int requestid, int loanid, int accountid, String startdate, String enddate, int repaymentdays) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var genson = new GensonBuilder().withConverters(new LocalDateConverter()).create();
		var res = bookNewLoan(requestid, loanid, accountid, genson.deserialize("\"" + startdate + "\"", LocalDate.class), genson.deserialize("\"" + enddate + "\"", LocalDate.class), repaymentdays);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean bookNewLoan(int requestid, int loanid, int accountid, LocalDate startdate, LocalDate enddate, int repaymentdays) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get loan
		Loan loan = null;
		//no nested iterator --  iterator: any previous:any
		for (Loan loa : (List<Loan>)EntityManager.getAllInstancesOf(Loan.class))
		{
			if (loa.getLoanID() == loanid)
			{
				loan = loa;
				break;
			}
				
			
		}
		//Get r
		LoanRequest r = null;
		//no nested iterator --  iterator: any previous:any
		for (LoanRequest lr : (List<LoanRequest>)EntityManager.getAllInstancesOf(LoanRequest.class))
		{
			if (lr.getRequestID() == requestid)
			{
				r = lr;
				break;
			}
				
			
		}
		//Get la
		LoanAccount la = null;
		//no nested iterator --  iterator: any previous:any
		for (LoanAccount lacc : (List<LoanAccount>)EntityManager.getAllInstancesOf(LoanAccount.class))
		{
			if (lacc.getLoanAccountID() == accountid)
			{
				la = lacc;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
		/* service reference */
		/* service temp attribute */
		/* objects in definition */
		LoanAccount Pre_la = SerializationUtils.clone(la);

		/* check precondition */
		if (StandardOPs.oclIsundefined(loan) == true && StandardOPs.oclIsundefined(r) == false) 
		{ 
			/* Logic here */
			Loan loa = null;
			LoanAccount lacc = null;
			loa = (Loan) EntityManager.createObject("Loan");
			loa.setLoanID(loanid);
			loa.setStartDate(startdate);
			loa.setEndDate(enddate);
			loa.setRePaymentDays(repaymentdays);
			loa.setStatus(LoanStatus.LSOPEN);
			loa.setRepaymentAmount(r.getLoanAmount());
			loa.setCurrentOverDueDate(startdate.plusDays(repaymentdays));
			if (StandardOPs.oclIsundefined(la) == true)
			{
				lacc = services.createLoanAccount(accountid);
				EntityManager.addObject("LoanAccount", lacc);
				lacc.setBalance(r.getLoanAmount());
				loa.setBelongedLoanAccount(lacc);
			}
			else
			{
			 	la.setBalance(la.getBalance()+r.getLoanAmount());
			}
			services.transferFunds(accountid, r.getLoanAmount());
			loa.setRemainAmountToPay(r.getLoanAmount());
			EntityManager.addObject("Loan", loa);
			r.setApprovalLoan(loa);
			loa.setReferedLoanRequest(r);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			loa.getLoanID() == loanid
			 && 
			loa.getStartDate().equals(startdate)
			 && 
			loa.getEndDate().equals(enddate)
			 && 
			loa.getRePaymentDays() == repaymentdays
			 && 
			loa.getStatus() == LoanStatus.LSOPEN
			 && 
			loa.getRepaymentAmount() == r.getLoanAmount()
			 && 
			loa.getCurrentOverDueDate().equals(startdate.plusDays(repaymentdays))
			 && 
			(StandardOPs.oclIsundefined(la) == true ? true
			 && 
			StandardOPs.includes(((List<LoanAccount>)EntityManager.getAllInstancesOf(LoanAccount.class)), lacc)
			 && 
			lacc.getBalance() == r.getLoanAmount()
			 && 
			loa.getBelongedLoanAccount() == lacc : la.getBalance() == Pre_la.getBalance()+r.getLoanAmount())
			 && 
			true
			 && 
			loa.getRemainAmountToPay() == r.getLoanAmount()
			 && 
			StandardOPs.includes(((List<Loan>)EntityManager.getAllInstancesOf(Loan.class)), loa)
			 && 
			r.getApprovalLoan() == loa
			 && 
			loa.getReferedLoanRequest() == r
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : r la lacc loa
		//all relevant entities : LoanRequest LoanAccount LoanAccount LoanAccount
	} 
	 
	static {opINVRelatedEntity.put("bookNewLoan", Arrays.asList("LoanRequest","LoanAccount","LoanAccount","LoanAccount"));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean generateStandardPaymentNotice(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = generateStandardPaymentNotice();
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean generateStandardPaymentNotice() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get loans
		List<Loan> loans = new LinkedList<>();
		//no nested iterator --  iterator: select previous:select
		for (Loan loa : (List<Loan>)EntityManager.getAllInstancesOf(Loan.class))
		{
			if (loa.getStatus() == LoanStatus.LSOPEN && LocalDate.now().plusDays(3).isAfter(loa.getCurrentOverDueDate()))
			{
				loans.add(loa);
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(loans) == false) 
		{ 
			/* Logic here */
			//no nested iterator --  iterator: forAll
			for (Loan l : loans)
			{
				services.sendEmail(l.getReferedLoanRequest().getEmail(), "OverDueSoon", "You account is OverDueSoon");
			}
			
			
			refresh();
			// post-condition checking
			if (!(((BooleanSupplier) () -> {							
				for (Loan l : loans) {
					if (!(true)) {
						return false;
					}
				}
				return true;
			}).getAsBoolean()
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : l
		//all relevant entities : Loan
	} 
	 
	static {opINVRelatedEntity.put("generateStandardPaymentNotice", Arrays.asList("Loan"));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean generateLateNotice(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = generateLateNotice();
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean generateLateNotice() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get loans
		List<Loan> loans = new LinkedList<>();
		//no nested iterator --  iterator: select previous:select
		for (Loan loa : (List<Loan>)EntityManager.getAllInstancesOf(Loan.class))
		{
			if (loa.getStatus() == LoanStatus.LSOPEN && LocalDate.now().isAfter(loa.getCurrentOverDueDate()))
			{
				loans.add(loa);
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(loans) == false) 
		{ 
			/* Logic here */
			//no nested iterator --  iterator: forAll
			for (Loan l : loans)
			{
				services.sendEmail(l.getReferedLoanRequest().getEmail(), "OverDued", "You are overdued, please repayment ASAP");
			}
			
			
			refresh();
			// post-condition checking
			if (!(((BooleanSupplier) () -> {							
				for (Loan l : loans) {
					if (!(true)) {
						return false;
					}
				}
				return true;
			}).getAsBoolean()
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : l
		//all relevant entities : Loan
	} 
	 
	static {opINVRelatedEntity.put("generateLateNotice", Arrays.asList("Loan"));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean listBookedLoans(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = listBookedLoans();
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean listBookedLoans() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (true) 
		{ 
			/* Logic here */
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
	} 
	 
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean loanPayment(final Context ctx, int loanid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = loanPayment(loanid);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean loanPayment(int loanid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get loan
		Loan loan = null;
		//no nested iterator --  iterator: any previous:any
		for (Loan loa : (List<Loan>)EntityManager.getAllInstancesOf(Loan.class))
		{
			if (loa.getLoanID() == loanid)
			{
				loan = loa;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
		/* service reference */
		/* service temp attribute */
		/* objects in definition */
		Loan Pre_loan = SerializationUtils.clone(loan);

		/* check precondition */
		if (StandardOPs.oclIsundefined(loan) == false && loan.getStatus() == LoanStatus.LSOPEN) 
		{ 
			/* Logic here */
			loan.setRemainAmountToPay(loan.getRemainAmountToPay()-loan.getRepaymentAmount());
			
			
			refresh();
			// post-condition checking
			if (!(loan.getRemainAmountToPay() == Pre_loan.getRemainAmountToPay()-loan.getRepaymentAmount()
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : loan
		//all relevant entities : Loan
	} 
	 
	static {opINVRelatedEntity.put("loanPayment", Arrays.asList("Loan"));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean closeOutLoan(final Context ctx, int loanid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = closeOutLoan(loanid);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean closeOutLoan(int loanid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get loan
		Loan loan = null;
		//no nested iterator --  iterator: any previous:any
		for (Loan loa : (List<Loan>)EntityManager.getAllInstancesOf(Loan.class))
		{
			if (loa.getLoanID() == loanid)
			{
				loan = loa;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(loan) == false && loan.getStatus() == LoanStatus.LSOPEN) 
		{ 
			/* Logic here */
			loan.setStatus(LoanStatus.CLOSED);
			
			
			refresh();
			// post-condition checking
			if (!(loan.getStatus() == LoanStatus.CLOSED
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : loan
		//all relevant entities : Loan
	} 
	 
	static {opINVRelatedEntity.put("closeOutLoan", Arrays.asList("Loan"));}
	
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
