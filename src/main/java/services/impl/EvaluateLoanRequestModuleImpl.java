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

public class EvaluateLoanRequestModuleImpl implements EvaluateLoanRequestModule, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public EvaluateLoanRequestModuleImpl() {
		services = new ThirdPartyServicesImpl();
	}

	
	//Shared variable from system services
	
	/* Shared variable from system services and get()/set() methods */
			
	/* all get and set functions for temp property*/
				
	
	
	/* Generate inject for sharing temp variables between use cases in system service */
	public void refresh() {
		LoanProcessingSystemSystem loanprocessingsystemsystem_service = (LoanProcessingSystemSystem) ServiceManager.getAllInstancesOf("LoanProcessingSystemSystem").get(0);
	}
	
	/* Generate buiness logic according to functional requirement */
	@SuppressWarnings("unchecked")
	public List<LoanRequest> listTenLoanRequest() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get rs
		List<LoanRequest> rs = new LinkedList<>();
		//no nested iterator --  iterator: select previous:select
		for (LoanRequest r : (List<LoanRequest>)EntityManager.getAllInstancesOf("LoanRequest"))
		{
			if (r.getStatus() == LoanRequestStatus.REFERENCESVALIDATED)
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
	 
	static {opINVRelatedEntity.put("listTenLoanRequest", Arrays.asList(""));}
	
	@SuppressWarnings("unchecked")
	public LoanRequest chooseOneForReview(int requestid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get rs
		LoanRequest rs = null;
		//no nested iterator --  iterator: any previous:any
		for (LoanRequest r : this.getCurrentLoanRequests())
		{
			if (r.getRequestID() == requestid)
			{
				rs = r;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(rs) == false) 
		{ 
			/* Logic here */
			this.setCurrentLoanRequest(rs);
			
			
			refresh();
			// post-condition checking
			if (!(this.getCurrentLoanRequest() == rs
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
	 
	static {opINVRelatedEntity.put("chooseOneForReview", Arrays.asList(""));}
	
	@SuppressWarnings("unchecked")
	public CreditHistory checkCreditHistory() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(this.getCurrentLoanRequest()) == false && StandardOPs.oclIsundefined(CurrentLoanRequest.getRequestedCreditHistory()) == false) 
		{ 
			/* Logic here */
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
			refresh(); return CurrentLoanRequest.getRequestedCreditHistory();
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : CurrentLoanRequest
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("checkCreditHistory", Arrays.asList(""));}
	
	@SuppressWarnings("unchecked")
	public CheckingAccount reviewCheckingAccount() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(this.getCurrentLoanRequest()) == false && StandardOPs.oclIsundefined(CurrentLoanRequest.getRequestedCAHistory()) == false) 
		{ 
			/* Logic here */
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
			refresh(); return CurrentLoanRequest.getRequestedCAHistory();
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : CurrentLoanRequest
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("reviewCheckingAccount", Arrays.asList(""));}
	
	@SuppressWarnings("unchecked")
	public List<LoanTerm> listAvaiableLoanTerm() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
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
			
			refresh(); return ((List<LoanTerm>)EntityManager.getAllInstancesOf("LoanTerm"));
		}
		else
		{
			throw new PreconditionException();
		}
	} 
	 
	
	@SuppressWarnings("unchecked")
	public boolean addLoanTerm(int termid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get loanterm
		LoanTerm loanterm = null;
		//no nested iterator --  iterator: any previous:any
		for (LoanTerm loa : (List<LoanTerm>)EntityManager.getAllInstancesOf("LoanTerm"))
		{
			if (loa.getItemID() == termid)
			{
				loanterm = loa;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(this.getCurrentLoanRequest()) == false && StandardOPs.oclIsundefined(loanterm) == false) 
		{ 
			/* Logic here */
			CurrentLoanRequest.addAttachedLoanTerms(loanterm);
			
			
			refresh();
			// post-condition checking
			if (!(StandardOPs.includes(CurrentLoanRequest.getAttachedLoanTerms(), loanterm)
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
		//all relevant vars : loanterm
		//all relevant entities : LoanTerm
	} 
	 
	static {opINVRelatedEntity.put("addLoanTerm", Arrays.asList("LoanTerm"));}
	
	@SuppressWarnings("unchecked")
	public boolean approveLoanRequest() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(this.getCurrentLoanRequest()) == false) 
		{ 
			/* Logic here */
			this.getCurrentLoanRequest().setStatus(LoanRequestStatus.APPROVED);
			
			
			refresh();
			// post-condition checking
			if (!(this.getCurrentLoanRequest().getStatus() == LoanRequestStatus.APPROVED
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
		//all relevant vars : this
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("approveLoanRequest", Arrays.asList(""));}
	
	
	
	
	/* temp property for controller */
	private LoanRequest CurrentLoanRequest;
	private List<LoanRequest> CurrentLoanRequests;
			
	/* all get and set functions for temp property*/
	public LoanRequest getCurrentLoanRequest() {
		return CurrentLoanRequest;
	}	
	
	public void setCurrentLoanRequest(LoanRequest currentloanrequest) {
		this.CurrentLoanRequest = currentloanrequest;
	}
	public List<LoanRequest> getCurrentLoanRequests() {
		return CurrentLoanRequests;
	}	
	
	public void setCurrentLoanRequests(List<LoanRequest> currentloanrequests) {
		this.CurrentLoanRequests = currentloanrequests;
	}
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
