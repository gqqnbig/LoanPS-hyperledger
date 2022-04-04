package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;
import org.hyperledger.fabric.contract.annotation.*;

@DataType()
public class LoanRequest implements Serializable {
	
	/* all primary attributes */
	@Property()
	private LoanRequestStatus status;
	@Property()
	private int requestID;
	@Property()
	private String name;
	@Property()
	private float loanAmount;
	@Property()
	private String loanPurpose;
	@Property()
	private float income;
	@Property()
	private int phoneNumber;
	@Property()
	private String postalAddress;
	@Property()
	private int zipCode;
	@Property()
	private String email;
	@Property()
	private String workReferences;
	@Property()
	private String creditReferences;
	@Property()
	private int checkingAccountNumber;
	@Property()
	private int securityNumber;
	@Property()
	private int creditScore;
	
	/* all references */
	private Loan ApprovalLoan; 
	private CheckingAccount RequestedCAHistory; 
	private CreditHistory RequestedCreditHistory; 
	private ApprovalLetter AttachedApprovalLetter; 
	private LoanAgreement AttachedLoanAgreement; 
	private List<LoanTerm> AttachedLoanTerms = new LinkedList<LoanTerm>(); 
	
	/* all get and set functions */
	public LoanRequestStatus getStatus() {
		return status;
	}	
	
	public void setStatus(LoanRequestStatus status) {
		this.status = status;
	}
	public int getRequestID() {
		return requestID;
	}	
	
	public void setRequestID(int requestid) {
		this.requestID = requestid;
	}
	public String getName() {
		return name;
	}	
	
	public void setName(String name) {
		this.name = name;
	}
	public float getLoanAmount() {
		return loanAmount;
	}	
	
	public void setLoanAmount(float loanamount) {
		this.loanAmount = loanamount;
	}
	public String getLoanPurpose() {
		return loanPurpose;
	}	
	
	public void setLoanPurpose(String loanpurpose) {
		this.loanPurpose = loanpurpose;
	}
	public float getIncome() {
		return income;
	}	
	
	public void setIncome(float income) {
		this.income = income;
	}
	public int getPhoneNumber() {
		return phoneNumber;
	}	
	
	public void setPhoneNumber(int phonenumber) {
		this.phoneNumber = phonenumber;
	}
	public String getPostalAddress() {
		return postalAddress;
	}	
	
	public void setPostalAddress(String postaladdress) {
		this.postalAddress = postaladdress;
	}
	public int getZipCode() {
		return zipCode;
	}	
	
	public void setZipCode(int zipcode) {
		this.zipCode = zipcode;
	}
	public String getEmail() {
		return email;
	}	
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWorkReferences() {
		return workReferences;
	}	
	
	public void setWorkReferences(String workreferences) {
		this.workReferences = workreferences;
	}
	public String getCreditReferences() {
		return creditReferences;
	}	
	
	public void setCreditReferences(String creditreferences) {
		this.creditReferences = creditreferences;
	}
	public int getCheckingAccountNumber() {
		return checkingAccountNumber;
	}	
	
	public void setCheckingAccountNumber(int checkingaccountnumber) {
		this.checkingAccountNumber = checkingaccountnumber;
	}
	public int getSecurityNumber() {
		return securityNumber;
	}	
	
	public void setSecurityNumber(int securitynumber) {
		this.securityNumber = securitynumber;
	}
	public int getCreditScore() {
		return creditScore;
	}	
	
	public void setCreditScore(int creditscore) {
		this.creditScore = creditscore;
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
		
		if (creditScore >= 0) {
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
