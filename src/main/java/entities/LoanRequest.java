package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class LoanRequest implements Serializable {
	
	/* all primary attributes */
	private LoanRequestStatus Status;
	private int RequestID;
	private String Name;
	private float LoanAmount;
	private String LoanPurpose;
	private float Income;
	private int PhoneNumber;
	private String PostalAddress;
	private int ZipCode;
	private String Email;
	private String WorkReferences;
	private String CreditReferences;
	private int CheckingAccountNumber;
	private int SecurityNumber;
	private int CreditScore;
	
	/* all references */
	private Loan ApprovalLoan; 
	private CheckingAccount RequestedCAHistory; 
	private CreditHistory RequestedCreditHistory; 
	private ApprovalLetter AttachedApprovalLetter; 
	private LoanAgreement AttachedLoanAgreement; 
	private List<LoanTerm> AttachedLoanTerms = new LinkedList<LoanTerm>(); 
	
	/* all get and set functions */
	public LoanRequestStatus getStatus() {
		return Status;
	}	
	
	public void setStatus(LoanRequestStatus status) {
		this.Status = status;
	}
	public int getRequestID() {
		return RequestID;
	}	
	
	public void setRequestID(int requestid) {
		this.RequestID = requestid;
	}
	public String getName() {
		return Name;
	}	
	
	public void setName(String name) {
		this.Name = name;
	}
	public float getLoanAmount() {
		return LoanAmount;
	}	
	
	public void setLoanAmount(float loanamount) {
		this.LoanAmount = loanamount;
	}
	public String getLoanPurpose() {
		return LoanPurpose;
	}	
	
	public void setLoanPurpose(String loanpurpose) {
		this.LoanPurpose = loanpurpose;
	}
	public float getIncome() {
		return Income;
	}	
	
	public void setIncome(float income) {
		this.Income = income;
	}
	public int getPhoneNumber() {
		return PhoneNumber;
	}	
	
	public void setPhoneNumber(int phonenumber) {
		this.PhoneNumber = phonenumber;
	}
	public String getPostalAddress() {
		return PostalAddress;
	}	
	
	public void setPostalAddress(String postaladdress) {
		this.PostalAddress = postaladdress;
	}
	public int getZipCode() {
		return ZipCode;
	}	
	
	public void setZipCode(int zipcode) {
		this.ZipCode = zipcode;
	}
	public String getEmail() {
		return Email;
	}	
	
	public void setEmail(String email) {
		this.Email = email;
	}
	public String getWorkReferences() {
		return WorkReferences;
	}	
	
	public void setWorkReferences(String workreferences) {
		this.WorkReferences = workreferences;
	}
	public String getCreditReferences() {
		return CreditReferences;
	}	
	
	public void setCreditReferences(String creditreferences) {
		this.CreditReferences = creditreferences;
	}
	public int getCheckingAccountNumber() {
		return CheckingAccountNumber;
	}	
	
	public void setCheckingAccountNumber(int checkingaccountnumber) {
		this.CheckingAccountNumber = checkingaccountnumber;
	}
	public int getSecurityNumber() {
		return SecurityNumber;
	}	
	
	public void setSecurityNumber(int securitynumber) {
		this.SecurityNumber = securitynumber;
	}
	public int getCreditScore() {
		return CreditScore;
	}	
	
	public void setCreditScore(int creditscore) {
		this.CreditScore = creditscore;
	}
	
	/* all functions for reference*/
	public Loan getApprovalLoan() {
		return ApprovalLoan;
	}	
	
	public void setApprovalLoan(Loan loan) {
		this.ApprovalLoan = loan;
	}			
	public CheckingAccount getRequestedCAHistory() {
		return RequestedCAHistory;
	}	
	
	public void setRequestedCAHistory(CheckingAccount checkingaccount) {
		this.RequestedCAHistory = checkingaccount;
	}			
	public CreditHistory getRequestedCreditHistory() {
		return RequestedCreditHistory;
	}	
	
	public void setRequestedCreditHistory(CreditHistory credithistory) {
		this.RequestedCreditHistory = credithistory;
	}			
	public ApprovalLetter getAttachedApprovalLetter() {
		return AttachedApprovalLetter;
	}	
	
	public void setAttachedApprovalLetter(ApprovalLetter approvalletter) {
		this.AttachedApprovalLetter = approvalletter;
	}			
	public LoanAgreement getAttachedLoanAgreement() {
		return AttachedLoanAgreement;
	}	
	
	public void setAttachedLoanAgreement(LoanAgreement loanagreement) {
		this.AttachedLoanAgreement = loanagreement;
	}			
	public List<LoanTerm> getAttachedLoanTerms() {
		return AttachedLoanTerms;
	}	
	
	public void addAttachedLoanTerms(LoanTerm loanterm) {
		this.AttachedLoanTerms.add(loanterm);
	}
	
	public void deleteAttachedLoanTerms(LoanTerm loanterm) {
		this.AttachedLoanTerms.remove(loanterm);
	}
	

	/* invarints checking*/
	public boolean LoanRequest_UniqueRequestID() {
		
		if (StandardOPs.isUnique(((List<LoanRequest>)EntityManager.getAllInstancesOf("LoanRequest")), "RequestID")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean LoanRequest_CreditScoreGreatAndEqualZero() {
		
		if (CreditScore >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (LoanRequest_UniqueRequestID() && LoanRequest_CreditScoreGreatAndEqualZero()) {
			return true;
		} else {
			return false;
		}
	}	
	
	//check invariant by inv name
	public boolean checkInvariant(String INVname) {
		
		try {
			Method m = this.getClass().getDeclaredMethod(INVname);
			return (boolean) m.invoke(this);
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	
	}	
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("LoanRequest_UniqueRequestID","LoanRequest_CreditScoreGreatAndEqualZero"));

}
