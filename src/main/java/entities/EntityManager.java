package entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.lang.reflect.Method;
import java.util.Map;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.File;

public class EntityManager {

	private static Map<String, List> AllInstance = new HashMap<String, List>();
	
	private static List<LoanRequest> LoanRequestInstances = new LinkedList<LoanRequest>();
	private static List<Loan> LoanInstances = new LinkedList<Loan>();
	private static List<LoanTerm> LoanTermInstances = new LinkedList<LoanTerm>();
	private static List<CheckingAccount> CheckingAccountInstances = new LinkedList<CheckingAccount>();
	private static List<CreditHistory> CreditHistoryInstances = new LinkedList<CreditHistory>();
	private static List<LoanAccount> LoanAccountInstances = new LinkedList<LoanAccount>();
	private static List<ApprovalLetter> ApprovalLetterInstances = new LinkedList<ApprovalLetter>();
	private static List<LoanAgreement> LoanAgreementInstances = new LinkedList<LoanAgreement>();

	
	/* Put instances list into Map */
	static {
		AllInstance.put("LoanRequest", LoanRequestInstances);
		AllInstance.put("Loan", LoanInstances);
		AllInstance.put("LoanTerm", LoanTermInstances);
		AllInstance.put("CheckingAccount", CheckingAccountInstances);
		AllInstance.put("CreditHistory", CreditHistoryInstances);
		AllInstance.put("LoanAccount", LoanAccountInstances);
		AllInstance.put("ApprovalLetter", ApprovalLetterInstances);
		AllInstance.put("LoanAgreement", LoanAgreementInstances);
	} 
		
	/* Save State */
	public static void save(File file) {
		
		try {
			
			ObjectOutputStream stateSave = new ObjectOutputStream(new FileOutputStream(file));
			
			stateSave.writeObject(LoanRequestInstances);
			stateSave.writeObject(LoanInstances);
			stateSave.writeObject(LoanTermInstances);
			stateSave.writeObject(CheckingAccountInstances);
			stateSave.writeObject(CreditHistoryInstances);
			stateSave.writeObject(LoanAccountInstances);
			stateSave.writeObject(ApprovalLetterInstances);
			stateSave.writeObject(LoanAgreementInstances);
			
			stateSave.close();
					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/* Load State */
	public static void load(File file) {
		
		try {
			
			ObjectInputStream stateLoad = new ObjectInputStream(new FileInputStream(file));
			
			try {
				
				LoanRequestInstances =  (List<LoanRequest>) stateLoad.readObject();
				AllInstance.put("LoanRequest", LoanRequestInstances);
				LoanInstances =  (List<Loan>) stateLoad.readObject();
				AllInstance.put("Loan", LoanInstances);
				LoanTermInstances =  (List<LoanTerm>) stateLoad.readObject();
				AllInstance.put("LoanTerm", LoanTermInstances);
				CheckingAccountInstances =  (List<CheckingAccount>) stateLoad.readObject();
				AllInstance.put("CheckingAccount", CheckingAccountInstances);
				CreditHistoryInstances =  (List<CreditHistory>) stateLoad.readObject();
				AllInstance.put("CreditHistory", CreditHistoryInstances);
				LoanAccountInstances =  (List<LoanAccount>) stateLoad.readObject();
				AllInstance.put("LoanAccount", LoanAccountInstances);
				ApprovalLetterInstances =  (List<ApprovalLetter>) stateLoad.readObject();
				AllInstance.put("ApprovalLetter", ApprovalLetterInstances);
				LoanAgreementInstances =  (List<LoanAgreement>) stateLoad.readObject();
				AllInstance.put("LoanAgreement", LoanAgreementInstances);
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	/* create object */  
	public static Object createObject(String Classifer) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method createObjectMethod = c.getDeclaredMethod("create" + Classifer + "Object");
			return createObjectMethod.invoke(c);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/* add object */  
	public static Object addObject(String Classifer, Object ob) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method addObjectMethod = c.getDeclaredMethod("add" + Classifer + "Object", Class.forName("entities." + Classifer));
			return  (boolean) addObjectMethod.invoke(c, ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}	
	
	/* add objects */  
	public static Object addObjects(String Classifer, List obs) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method addObjectsMethod = c.getDeclaredMethod("add" + Classifer + "Objects", Class.forName("java.util.List"));
			return  (boolean) addObjectsMethod.invoke(c, obs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/* Release object */
	public static boolean deleteObject(String Classifer, Object ob) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method deleteObjectMethod = c.getDeclaredMethod("delete" + Classifer + "Object", Class.forName("entities." + Classifer));
			return  (boolean) deleteObjectMethod.invoke(c, ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/* Release objects */
	public static boolean deleteObjects(String Classifer, List obs) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method deleteObjectMethod = c.getDeclaredMethod("delete" + Classifer + "Objects", Class.forName("java.util.List"));
			return  (boolean) deleteObjectMethod.invoke(c, obs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}		 	
	
	 /* Get all objects belongs to same class */
	public static List getAllInstancesOf(String ClassName) {
			 return AllInstance.get(ClassName);
	}	

   /* Sub-create object */
	public static LoanRequest createLoanRequestObject() {
		LoanRequest o = new LoanRequest();
		return o;
	}
	
	public static boolean addLoanRequestObject(LoanRequest o) {
		return LoanRequestInstances.add(o);
	}
	
	public static boolean addLoanRequestObjects(List<LoanRequest> os) {
		return LoanRequestInstances.addAll(os);
	}
	
	public static boolean deleteLoanRequestObject(LoanRequest o) {
		return LoanRequestInstances.remove(o);
	}
	
	public static boolean deleteLoanRequestObjects(List<LoanRequest> os) {
		return LoanRequestInstances.removeAll(os);
	}
	public static Loan createLoanObject() {
		Loan o = new Loan();
		return o;
	}
	
	public static boolean addLoanObject(Loan o) {
		return LoanInstances.add(o);
	}
	
	public static boolean addLoanObjects(List<Loan> os) {
		return LoanInstances.addAll(os);
	}
	
	public static boolean deleteLoanObject(Loan o) {
		return LoanInstances.remove(o);
	}
	
	public static boolean deleteLoanObjects(List<Loan> os) {
		return LoanInstances.removeAll(os);
	}
	public static LoanTerm createLoanTermObject() {
		LoanTerm o = new LoanTerm();
		return o;
	}
	
	public static boolean addLoanTermObject(LoanTerm o) {
		return LoanTermInstances.add(o);
	}
	
	public static boolean addLoanTermObjects(List<LoanTerm> os) {
		return LoanTermInstances.addAll(os);
	}
	
	public static boolean deleteLoanTermObject(LoanTerm o) {
		return LoanTermInstances.remove(o);
	}
	
	public static boolean deleteLoanTermObjects(List<LoanTerm> os) {
		return LoanTermInstances.removeAll(os);
	}
	public static CheckingAccount createCheckingAccountObject() {
		CheckingAccount o = new CheckingAccount();
		return o;
	}
	
	public static boolean addCheckingAccountObject(CheckingAccount o) {
		return CheckingAccountInstances.add(o);
	}
	
	public static boolean addCheckingAccountObjects(List<CheckingAccount> os) {
		return CheckingAccountInstances.addAll(os);
	}
	
	public static boolean deleteCheckingAccountObject(CheckingAccount o) {
		return CheckingAccountInstances.remove(o);
	}
	
	public static boolean deleteCheckingAccountObjects(List<CheckingAccount> os) {
		return CheckingAccountInstances.removeAll(os);
	}
	public static CreditHistory createCreditHistoryObject() {
		CreditHistory o = new CreditHistory();
		return o;
	}
	
	public static boolean addCreditHistoryObject(CreditHistory o) {
		return CreditHistoryInstances.add(o);
	}
	
	public static boolean addCreditHistoryObjects(List<CreditHistory> os) {
		return CreditHistoryInstances.addAll(os);
	}
	
	public static boolean deleteCreditHistoryObject(CreditHistory o) {
		return CreditHistoryInstances.remove(o);
	}
	
	public static boolean deleteCreditHistoryObjects(List<CreditHistory> os) {
		return CreditHistoryInstances.removeAll(os);
	}
	public static LoanAccount createLoanAccountObject() {
		LoanAccount o = new LoanAccount();
		return o;
	}
	
	public static boolean addLoanAccountObject(LoanAccount o) {
		return LoanAccountInstances.add(o);
	}
	
	public static boolean addLoanAccountObjects(List<LoanAccount> os) {
		return LoanAccountInstances.addAll(os);
	}
	
	public static boolean deleteLoanAccountObject(LoanAccount o) {
		return LoanAccountInstances.remove(o);
	}
	
	public static boolean deleteLoanAccountObjects(List<LoanAccount> os) {
		return LoanAccountInstances.removeAll(os);
	}
	public static ApprovalLetter createApprovalLetterObject() {
		ApprovalLetter o = new ApprovalLetter();
		return o;
	}
	
	public static boolean addApprovalLetterObject(ApprovalLetter o) {
		return ApprovalLetterInstances.add(o);
	}
	
	public static boolean addApprovalLetterObjects(List<ApprovalLetter> os) {
		return ApprovalLetterInstances.addAll(os);
	}
	
	public static boolean deleteApprovalLetterObject(ApprovalLetter o) {
		return ApprovalLetterInstances.remove(o);
	}
	
	public static boolean deleteApprovalLetterObjects(List<ApprovalLetter> os) {
		return ApprovalLetterInstances.removeAll(os);
	}
	public static LoanAgreement createLoanAgreementObject() {
		LoanAgreement o = new LoanAgreement();
		return o;
	}
	
	public static boolean addLoanAgreementObject(LoanAgreement o) {
		return LoanAgreementInstances.add(o);
	}
	
	public static boolean addLoanAgreementObjects(List<LoanAgreement> os) {
		return LoanAgreementInstances.addAll(os);
	}
	
	public static boolean deleteLoanAgreementObject(LoanAgreement o) {
		return LoanAgreementInstances.remove(o);
	}
	
	public static boolean deleteLoanAgreementObjects(List<LoanAgreement> os) {
		return LoanAgreementInstances.removeAll(os);
	}
  
}

