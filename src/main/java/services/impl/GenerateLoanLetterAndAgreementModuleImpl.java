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

@Contract
public class GenerateLoanLetterAndAgreementModuleImpl implements GenerateLoanLetterAndAgreementModule, Serializable, ContractInterface {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public GenerateLoanLetterAndAgreementModuleImpl() {
		services = new ThirdPartyServicesImpl();
	}

	
	//Shared variable from system services
	
	/* Shared variable from system services and get()/set() methods */
			
	/* all get and set functions for temp property*/
				
	
	
	/* Generate inject for sharing temp variables between use cases in system service */
	public void refresh() {
		LoanProcessingSystemSystem loanprocessingsystemsystem_service = (LoanProcessingSystemSystem) ServiceManager.getAllInstancesOf(LoanProcessingSystemSystem.class).get(0);
	}
	
	/* Generate buiness logic according to functional requirement */
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public LoanRequest[] listApprovalRequest(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = listApprovalRequest();
		return res.toArray(LoanRequest[]::new);
	}

	@SuppressWarnings("unchecked")
	public List<LoanRequest> listApprovalRequest() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get rs
		List<LoanRequest> rs = new LinkedList<>();
		//no nested iterator --  iterator: select previous:select
		for (LoanRequest r : (List<LoanRequest>)EntityManager.getAllInstancesOf(LoanRequest.class))
		{
			if (r.getStatus() == LoanRequestStatus.APPROVED)
			{
				rs.add(r);
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(rs) == false) 
		{ 
			/* Logic here */
			this.setCurrentLoanRequests(rs);
			
			
			refresh();
			// post-condition checking
			if (!(this.getCurrentLoanRequests() == rs
			 && 
			true)) {
				throw new PostconditionException();
			}
			
			refresh(); return rs;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : this
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("listApprovalRequest", Arrays.asList(""));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean genereateApprovalLetter(final Context ctx, int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = genereateApprovalLetter(id);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean genereateApprovalLetter(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get r
		LoanRequest r = null;
		//no nested iterator --  iterator: any previous:any
		for (LoanRequest lr : (List<LoanRequest>)EntityManager.getAllInstancesOf(LoanRequest.class))
		{
			if (lr.getRequestID() == id)
			{
				r = lr;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(r) == false) 
		{ 
			/* Logic here */
			ApprovalLetter l = null;
			l = (ApprovalLetter) EntityManager.createObject("ApprovalLetter");
			l.setContent("ApprovalLetterContent");
			r.setAttachedApprovalLetter(l);
			this.setCurrentLoanRequest(r);
			EntityManager.addObject("ApprovalLetter", l);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			l.getContent().equals("ApprovalLetterContent")
			 && 
			r.getAttachedApprovalLetter() == l
			 && 
			this.getCurrentLoanRequest() == r
			 && 
			StandardOPs.includes(((List<ApprovalLetter>)EntityManager.getAllInstancesOf(ApprovalLetter.class)), l)
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
		//all relevant vars : r this l
		//all relevant entities : LoanRequest  ApprovalLetter
	} 
	 
	static {opINVRelatedEntity.put("genereateApprovalLetter", Arrays.asList("LoanRequest","","ApprovalLetter"));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean emailToAppliant(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = emailToAppliant();
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean emailToAppliant() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(this.getCurrentLoanRequest()) == false) 
		{ 
			/* Logic here */
			services.sendEmail(currentLoanRequest.getEmail(), currentLoanRequest.getName(), "Your Loan Request was approved");
			
			
			refresh();
			// post-condition checking
			if (!(true
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
	} 
	 
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean generateLoanAgreement(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = generateLoanAgreement();
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean generateLoanAgreement() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(this.getCurrentLoanRequest()) == false) 
		{ 
			/* Logic here */
			LoanAgreement la = null;
			la = (LoanAgreement) EntityManager.createObject("LoanAgreement");
			la.setContent("Loan Agreement");
			this.getCurrentLoanRequest().setAttachedLoanAgreement(la);
			EntityManager.addObject("LoanAgreement", la);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			la.getContent().equals("Loan Agreement")
			 && 
			this.getCurrentLoanRequest().getAttachedLoanAgreement() == la
			 && 
			StandardOPs.includes(((List<LoanAgreement>)EntityManager.getAllInstancesOf(LoanAgreement.class)), la)
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
		//all relevant vars : la this
		//all relevant entities : LoanAgreement 
	} 
	 
	static {opINVRelatedEntity.put("generateLoanAgreement", Arrays.asList("LoanAgreement",""));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean printLoanAgreement(final Context ctx, int number) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = printLoanAgreement(number);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean printLoanAgreement(int number) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(this.getCurrentLoanRequest()) == false) 
		{ 
			/* Logic here */
			services.print(currentLoanRequest.getAttachedLoanAgreement().getContent(), number);
			
			
			refresh();
			// post-condition checking
			if (!(true
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
	} 
	 
	
	
	
	
	/* temp property for controller */
	private ApprovalLetter currentApprovalLetter;
	private LoanAgreement currentLoanAgreement;
	private LoanRequest currentLoanRequest;
	private List<LoanRequest> currentLoanRequests;
			
	/* all get and set functions for temp property*/
	public ApprovalLetter getCurrentApprovalLetter() {
		return currentApprovalLetter;
	}	
	
	public void setCurrentApprovalLetter(ApprovalLetter currentapprovalletter) {
		this.currentApprovalLetter = currentapprovalletter;
	}
	public LoanAgreement getCurrentLoanAgreement() {
		return currentLoanAgreement;
	}	
	
	public void setCurrentLoanAgreement(LoanAgreement currentloanagreement) {
		this.currentLoanAgreement = currentloanagreement;
	}
	public LoanRequest getCurrentLoanRequest() {
		return currentLoanRequest;
	}	
	
	public void setCurrentLoanRequest(LoanRequest currentloanrequest) {
		this.currentLoanRequest = currentloanrequest;
	}
	public List<LoanRequest> getCurrentLoanRequests() {
		return currentLoanRequests;
	}	
	
	public void setCurrentLoanRequests(List<LoanRequest> currentloanrequests) {
		this.currentLoanRequests = currentloanrequests;
	}
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
