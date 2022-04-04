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
import java.util.stream.*;

@Contract
public class EnterValidatedCreditReferencesModuleImpl implements EnterValidatedCreditReferencesModule, Serializable, ContractInterface {
	private static final Genson genson = new Genson();
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public EnterValidatedCreditReferencesModuleImpl() {
		services = new ThirdPartyServicesImpl();
	}

	
	//Shared variable from system services
	
	/* Shared variable from system services and get()/set() methods */
			
	/* all get and set functions for temp property*/
				
	
	
	/* Generate inject for sharing temp variables between use cases in system service */
	
	
	/* Generate buiness logic according to functional requirement */
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public LoanRequest[] listSubmitedLoanRequest(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = listSubmitedLoanRequest();
		return res.toArray(LoanRequest[]::new);
	}

	@SuppressWarnings("unchecked")
	public List<LoanRequest> listSubmitedLoanRequest() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get rs
		List<LoanRequest> rs = new LinkedList<>();
		//no nested iterator --  iterator: select previous:select
		for (LoanRequest r : (List<LoanRequest>)EntityManager.getAllInstancesOf(LoanRequest.class))
		{
			if (r.getStatus() == LoanRequestStatus.SUBMITTED)
			{
				rs.add(r);
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.size(rs) > 0) 
		{ 
			/* Logic here */
			this.setCurrentLoanRequests(rs);
			
			
			;
			// post-condition checking
			if (!(this.getCurrentLoanRequests() == rs
			 && 
			true)) {
				throw new PostconditionException();
			}
			
			; return rs;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : this
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("listSubmitedLoanRequest", Arrays.asList(""));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public LoanRequest chooseLoanRequest(final Context ctx, int requestid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = chooseLoanRequest(requestid);
		return res;
	}

	@SuppressWarnings("unchecked")
	public LoanRequest chooseLoanRequest(int requestid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
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
			
			
			;
			// post-condition checking
			if (!(this.getCurrentLoanRequest() == rs
			 && 
			true)) {
				throw new PostconditionException();
			}
			
			; return rs;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : this
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("chooseLoanRequest", Arrays.asList(""));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean markRequestValid(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = markRequestValid();
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean markRequestValid() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(this.getCurrentLoanRequest()) == false) 
		{ 
			/* Logic here */
			this.getCurrentLoanRequest().setStatus(LoanRequestStatus.REFERENCESVALIDATED);
			
			
			;
			// post-condition checking
			if (!(this.getCurrentLoanRequest().getStatus() == LoanRequestStatus.REFERENCESVALIDATED
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
		//all relevant vars : this
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("markRequestValid", Arrays.asList(""));}
	
	
	
	
	/* temp property for controller */
	private Object currentLoanRequestPK;
	private LoanRequest currentLoanRequest;
	private List<Object> currentLoanRequestsPKs;
	private List<LoanRequest> currentLoanRequests;
			
	/* all get and set functions for temp property*/
	public LoanRequest getCurrentLoanRequest() {
		return EntityManager.getLoanRequestByPK(getCurrentLoanRequestPK());
	}

	private Object getCurrentLoanRequestPK() {
		if (currentLoanRequestPK == null)
			currentLoanRequestPK = genson.deserialize(EntityManager.stub.getStringState("EnterValidatedCreditReferencesModuleImpl.currentLoanRequestPK"), Integer.class);

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
		EntityManager.stub.putStringState("EnterValidatedCreditReferencesModuleImpl.currentLoanRequestPK", json);
		//If we set currentLoanRequestPK to null, the getter thinks this fields is not initialized, thus will read the old value from chain.
		if (currentLoanRequestPK != null)
			this.currentLoanRequestPK = currentLoanRequestPK;
		else
			this.currentLoanRequestPK = EntityManager.getGuid();
	}
	public List<LoanRequest> getCurrentLoanRequests() {
		if (currentLoanRequests == null)
			currentLoanRequests = getCurrentLoanRequestsPKs().stream().map(EntityManager::getLoanRequestByPK).collect(Collectors.toList());
		return currentLoanRequests;
	}

	private List<Object> getCurrentLoanRequestsPKs() {
		if (currentLoanRequestsPKs == null)
			currentLoanRequestsPKs = (List) GensonHelper.deserializeList(genson, EntityManager.stub.getStringState("EnterValidatedCreditReferencesModuleImpl.currentLoanRequestsPKs"), Integer.class);
		return currentLoanRequestsPKs;
	}	
	
	public void setCurrentLoanRequests(List<LoanRequest> currentloanrequests) {
		setCurrentLoanRequestsPKs(currentloanrequests.stream().map(LoanRequest::getPK).collect(Collectors.toList()));
		this.currentLoanRequests = currentloanrequests;
	}

	private void setCurrentLoanRequestsPKs(List<Object> currentLoanRequestsPKs) {
		String json = genson.serialize(currentLoanRequestsPKs);
		EntityManager.stub.putStringState("EnterValidatedCreditReferencesModuleImpl.currentLoanRequestsPKs", json);
		this.currentLoanRequestsPKs = currentLoanRequestsPKs;
	}
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
