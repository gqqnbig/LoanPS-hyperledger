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
public class ManageLoanTermCRUDServiceImpl implements ManageLoanTermCRUDService, Serializable, ContractInterface {
	private static final Genson genson = new Genson();
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public ManageLoanTermCRUDServiceImpl() {
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
	public boolean createLoanTerm(final Context ctx, int itemid, String content) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = createLoanTerm(itemid, content);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean createLoanTerm(int itemid, String content) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get loanterm
		LoanTerm loanterm = null;
		//no nested iterator --  iterator: any previous:any
		for (LoanTerm loa : (List<LoanTerm>)EntityManager.getAllInstancesOf(LoanTerm.class))
		{
			if (loa.getItemID() == itemid)
			{
				loanterm = loa;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(loanterm) == true) 
		{ 
			/* Logic here */
			LoanTerm loa = null;
			loa = (LoanTerm) EntityManager.createObject("LoanTerm");
			loa.setItemID(itemid);
			loa.setContent(content);
			EntityManager.addObject("LoanTerm", loa);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			loa.getItemID() == itemid
			 && 
			loa.getContent() == content
			 && 
			StandardOPs.includes(((List<LoanTerm>)EntityManager.getAllInstancesOf(LoanTerm.class)), loa)
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
		//string parameters: [content]
		//all relevant vars : loa
		//all relevant entities : LoanTerm
	} 
	 
	static {opINVRelatedEntity.put("createLoanTerm", Arrays.asList("LoanTerm"));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public LoanTerm queryLoanTerm(final Context ctx, int itemid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = queryLoanTerm(itemid);
		return res;
	}

	@SuppressWarnings("unchecked")
	public LoanTerm queryLoanTerm(int itemid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get loanterm
		LoanTerm loanterm = null;
		//no nested iterator --  iterator: any previous:any
		for (LoanTerm loa : (List<LoanTerm>)EntityManager.getAllInstancesOf(LoanTerm.class))
		{
			if (loa.getItemID() == itemid)
			{
				loanterm = loa;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(loanterm) == false) 
		{ 
			/* Logic here */
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
			refresh(); return loanterm;
		}
		else
		{
			throw new PreconditionException();
		}
	} 
	 
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean modifyLoanTerm(final Context ctx, int itemid, String content) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = modifyLoanTerm(itemid, content);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean modifyLoanTerm(int itemid, String content) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get loanterm
		LoanTerm loanterm = null;
		//no nested iterator --  iterator: any previous:any
		for (LoanTerm loa : (List<LoanTerm>)EntityManager.getAllInstancesOf(LoanTerm.class))
		{
			if (loa.getItemID() == itemid)
			{
				loanterm = loa;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(loanterm) == false) 
		{ 
			/* Logic here */
			loanterm.setItemID(itemid);
			loanterm.setContent(content);
			
			
			refresh();
			// post-condition checking
			if (!(loanterm.getItemID() == itemid
			 && 
			loanterm.getContent() == content
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
		//string parameters: [content]
		//all relevant vars : loanterm
		//all relevant entities : LoanTerm
	} 
	 
	static {opINVRelatedEntity.put("modifyLoanTerm", Arrays.asList("LoanTerm"));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean deleteLoanTerm(final Context ctx, int itemid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = deleteLoanTerm(itemid);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean deleteLoanTerm(int itemid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get loanterm
		LoanTerm loanterm = null;
		//no nested iterator --  iterator: any previous:any
		for (LoanTerm loa : (List<LoanTerm>)EntityManager.getAllInstancesOf(LoanTerm.class))
		{
			if (loa.getItemID() == itemid)
			{
				loanterm = loa;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(loanterm) == false && StandardOPs.includes(((List<LoanTerm>)EntityManager.getAllInstancesOf(LoanTerm.class)), loanterm)) 
		{ 
			/* Logic here */
			EntityManager.deleteObject("LoanTerm", loanterm);
			
			
			refresh();
			// post-condition checking
			if (!(StandardOPs.excludes(((List<LoanTerm>)EntityManager.getAllInstancesOf(LoanTerm.class)), loanterm)
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
	 
	static {opINVRelatedEntity.put("deleteLoanTerm", Arrays.asList("LoanTerm"));}
	
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
