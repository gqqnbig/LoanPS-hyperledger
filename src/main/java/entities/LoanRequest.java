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
import com.owlike.genson.annotation.*;
import java.util.stream.*;

@DataType()
public class LoanRequest implements Serializable {
	public Object getPK() {
		return getRequestID();
	}
	
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
	@JsonProperty
	private Object ApprovalLoanPK;
	private Loan ApprovalLoan; 
	@JsonProperty
	private Object RequestedCAHistoryPK;
	/**
	 * CA means CheckingAccount.
	 */
	private CheckingAccount RequestedCAHistory; 
	@JsonProperty
	private Object RequestedCreditHistoryPK;
	private CreditHistory RequestedCreditHistory; 
	@JsonProperty
	private Object AttachedApprovalLetterPK;
	private ApprovalLetter AttachedApprovalLetter; 
	@JsonProperty
	private Object AttachedLoanAgreementPK;
	private LoanAgreement AttachedLoanAgreement; 
	@JsonProperty
	private List<Object> AttachedLoanTermsPKs = new LinkedList<>();
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
	@JsonIgnore
	public Loan getApprovalLoan() {
		if (ApprovalLoan == null)
			ApprovalLoan = EntityManager.getLoanByPK(ApprovalLoanPK);
		return ApprovalLoan;
	}	
	
	public void setApprovalLoan(Loan loan) {
		this.ApprovalLoan = loan;
		this.ApprovalLoanPK = loan.getPK();
	}			
	@JsonIgnore
	public CheckingAccount getRequestedCAHistory() {
		if (RequestedCAHistory == null)
			RequestedCAHistory = EntityManager.getCheckingAccountByPK(RequestedCAHistoryPK);
		return RequestedCAHistory;
	}	
	
	public void setRequestedCAHistory(CheckingAccount checkingaccount) {
		this.RequestedCAHistory = checkingaccount;
		this.RequestedCAHistoryPK = checkingaccount.getPK();
	}			
	@JsonIgnore
	public CreditHistory getRequestedCreditHistory() {
		if (RequestedCreditHistory == null)
			RequestedCreditHistory = EntityManager.getCreditHistoryByPK(RequestedCreditHistoryPK);
		return RequestedCreditHistory;
	}	
	
	public void setRequestedCreditHistory(CreditHistory credithistory) {
		this.RequestedCreditHistory = credithistory;
		this.RequestedCreditHistoryPK = credithistory.getPK();
	}			
	@JsonIgnore
	public ApprovalLetter getAttachedApprovalLetter() {
		if (AttachedApprovalLetter == null)
			AttachedApprovalLetter = EntityManager.getApprovalLetterByPK(AttachedApprovalLetterPK);
		return AttachedApprovalLetter;
	}	
	
	public void setAttachedApprovalLetter(ApprovalLetter approvalletter) {
		this.AttachedApprovalLetter = approvalletter;
		this.AttachedApprovalLetterPK = approvalletter.getPK();
	}			
	@JsonIgnore
	public LoanAgreement getAttachedLoanAgreement() {
		if (AttachedLoanAgreement == null)
			AttachedLoanAgreement = EntityManager.getLoanAgreementByPK(AttachedLoanAgreementPK);
		return AttachedLoanAgreement;
	}	
	
	public void setAttachedLoanAgreement(LoanAgreement loanagreement) {
		this.AttachedLoanAgreement = loanagreement;
		this.AttachedLoanAgreementPK = loanagreement.getPK();
	}			
	@JsonIgnore
	public List<LoanTerm> getAttachedLoanTerms() {
		if (AttachedLoanTerms == null)
			AttachedLoanTerms = AttachedLoanTermsPKs.stream().map(EntityManager::getLoanTermByPK).collect(Collectors.toList());
		return AttachedLoanTerms;
	}	
	
	public void addAttachedLoanTerms(LoanTerm loanterm) {
		getAttachedLoanTerms();
		this.AttachedLoanTermsPKs.add(loanterm.getPK());
		this.AttachedLoanTerms.add(loanterm);
	}
	
	public void deleteAttachedLoanTerms(LoanTerm loanterm) {
		getAttachedLoanTerms();
		this.AttachedLoanTermsPKs.remove(loanterm.getPK());
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
