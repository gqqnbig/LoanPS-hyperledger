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
public class GenerateLoanLetterAndAgreementModuleImpl implements GenerateLoanLetterAndAgreementModule, Serializable, ContractInterface {
	private static final Genson genson = new Genson();
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public GenerateLoanLetterAndAgreementModuleImpl() {
		services = new ThirdPartyServicesImpl();
	}

	
	//Shared variable from system services
	
	/* Shared variable from system services and get()/set() methods */
			
	/* all get and set functions for temp property*/
				
	
	
	/* Generate inject for sharing temp variables between use cases in system service */
	
	
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
			
			
			;
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
			services.sendEmail(getCurrentLoanRequest().getEmail(), getCurrentLoanRequest().getName(), "Your Loan Request was approved");
			
			
			;
			// post-condition checking
			if (!(true
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
			
			
			;
			// post-condition checking
			if (!(true && 
			la.getContent().equals("Loan Agreement")
			 && 
			this.getCurrentLoanRequest().getAttachedLoanAgreement() == la
			 && 
			StandardOPs.includes(((List<LoanAgreement>)EntityManager.getAllInstancesOf(LoanAgreement.class)), la)
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
			services.print(getCurrentLoanRequest().getAttachedLoanAgreement().getContent(), number);
			
			
			;
			// post-condition checking
			if (!(true
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
	} 
	 
	
	
	
	
	/* temp property for controller */
	private Object currentApprovalLetterPK;
	private ApprovalLetter currentApprovalLetter;
	private Object currentLoanAgreementPK;
	private LoanAgreement currentLoanAgreement;
	private Object currentLoanRequestPK;
	private LoanRequest currentLoanRequest;
	private List<Object> currentLoanRequestsPKs;
	private List<LoanRequest> currentLoanRequests;
			
	/* all get and set functions for temp property*/
	public ApprovalLetter getCurrentApprovalLetter() {
		return EntityManager.getApprovalLetterByPK(getCurrentApprovalLetterPK());
	}

	private Object getCurrentApprovalLetterPK() {
		if (currentApprovalLetterPK == null)
			currentApprovalLetterPK = genson.deserialize(EntityManager.stub.getStringState("GenerateLoanLetterAndAgreementModuleImpl.currentApprovalLetterPK"), String.class);

		return currentApprovalLetterPK;
	}	
	
	public void setCurrentApprovalLetter(ApprovalLetter currentapprovalletter) {
		if (currentapprovalletter != null)
			setCurrentApprovalLetterPK(currentapprovalletter.getPK());
		else
			setCurrentApprovalLetterPK(null);
		this.currentApprovalLetter = currentapprovalletter;
	}

	private void setCurrentApprovalLetterPK(Object currentApprovalLetterPK) {
		String json = genson.serialize(currentApprovalLetterPK);
		EntityManager.stub.putStringState("GenerateLoanLetterAndAgreementModuleImpl.currentApprovalLetterPK", json);
		//If we set currentApprovalLetterPK to null, the getter thinks this fields is not initialized, thus will read the old value from chain.
		if (currentApprovalLetterPK != null)
			this.currentApprovalLetterPK = currentApprovalLetterPK;
		else
			this.currentApprovalLetterPK = EntityManager.getGuid();
	}
	public LoanAgreement getCurrentLoanAgreement() {
		return EntityManager.getLoanAgreementByPK(getCurrentLoanAgreementPK());
	}

	private Object getCurrentLoanAgreementPK() {
		if (currentLoanAgreementPK == null)
			currentLoanAgreementPK = genson.deserialize(EntityManager.stub.getStringState("GenerateLoanLetterAndAgreementModuleImpl.currentLoanAgreementPK"), String.class);

		return currentLoanAgreementPK;
	}	
	
	public void setCurrentLoanAgreement(LoanAgreement currentloanagreement) {
		if (currentloanagreement != null)
			setCurrentLoanAgreementPK(currentloanagreement.getPK());
		else
			setCurrentLoanAgreementPK(null);
		this.currentLoanAgreement = currentloanagreement;
	}

	private void setCurrentLoanAgreementPK(Object currentLoanAgreementPK) {
		String json = genson.serialize(currentLoanAgreementPK);
		EntityManager.stub.putStringState("GenerateLoanLetterAndAgreementModuleImpl.currentLoanAgreementPK", json);
		//If we set currentLoanAgreementPK to null, the getter thinks this fields is not initialized, thus will read the old value from chain.
		if (currentLoanAgreementPK != null)
			this.currentLoanAgreementPK = currentLoanAgreementPK;
		else
			this.currentLoanAgreementPK = EntityManager.getGuid();
	}
	public LoanRequest getCurrentLoanRequest() {
		return EntityManager.getLoanRequestByPK(getCurrentLoanRequestPK());
	}

	private Object getCurrentLoanRequestPK() {
		if (currentLoanRequestPK == null)
			currentLoanRequestPK = genson.deserialize(EntityManager.stub.getStringState("GenerateLoanLetterAndAgreementModuleImpl.currentLoanRequestPK"), Integer.class);

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
		EntityManager.stub.putStringState("GenerateLoanLetterAndAgreementModuleImpl.currentLoanRequestPK", json);
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
			currentLoanRequestsPKs = (List) GensonHelper.deserializeList(genson, EntityManager.stub.getStringState("GenerateLoanLetterAndAgreementModuleImpl.currentLoanRequestsPKs"), Integer.class);
		return currentLoanRequestsPKs;
	}	
	
	public void setCurrentLoanRequests(List<LoanRequest> currentloanrequests) {
		setCurrentLoanRequestsPKs(currentloanrequests.stream().map(LoanRequest::getPK).collect(Collectors.toList()));
		this.currentLoanRequests = currentloanrequests;
	}

	private void setCurrentLoanRequestsPKs(List<Object> currentLoanRequestsPKs) {
		String json = genson.serialize(currentLoanRequestsPKs);
		EntityManager.stub.putStringState("GenerateLoanLetterAndAgreementModuleImpl.currentLoanRequestsPKs", json);
		this.currentLoanRequestsPKs = currentLoanRequestsPKs;
	}
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
