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
import converters.*;
import com.owlike.genson.annotation.*;

@DataType()
public class Loan implements Serializable {
	public Object getPK() {
		return getLoanID();
	}
	
	/* all primary attributes */
	@Property()
	private int loanID;
	@Property()
	private float remainAmountToPay;
	@Property()
	private LoanStatus status;
	@Property()
	private boolean isPaidinFull;
	@Property()
	private LocalDate startDate;
	@Property()
	private LocalDate endDate;
	@Property()
	private LocalDate currentOverDueDate;
	@Property()
	private int rePaymentDays;
	@Property()
	private float repaymentAmount;
	
	/* all references */
	@JsonProperty
	private Object ReferedLoanRequestPK;
	private LoanRequest ReferedLoanRequest; 
	@JsonProperty
	private Object BelongedLoanAccountPK;
	private LoanAccount BelongedLoanAccount; 
	
	/* all get and set functions */
	public int getLoanID() {
		return loanID;
	}	
	
	public void setLoanID(int loanid) {
		this.loanID = loanid;
	}
	public float getRemainAmountToPay() {
		return remainAmountToPay;
	}	
	
	public void setRemainAmountToPay(float remainamounttopay) {
		this.remainAmountToPay = remainamounttopay;
	}
	public LoanStatus getStatus() {
		return status;
	}	
	
	public void setStatus(LoanStatus status) {
		this.status = status;
	}
	public boolean getIsPaidinFull() {
		return isPaidinFull;
	}	
	
	public void setIsPaidinFull(boolean ispaidinfull) {
		this.isPaidinFull = ispaidinfull;
	}
	@JsonConverter(LocalDateConverter.class)
	public LocalDate getStartDate() {
		return startDate;
	}	
	
	@JsonConverter(LocalDateConverter.class)
	public void setStartDate(LocalDate startdate) {
		this.startDate = startdate;
	}
	@JsonConverter(LocalDateConverter.class)
	public LocalDate getEndDate() {
		return endDate;
	}	
	
	@JsonConverter(LocalDateConverter.class)
	public void setEndDate(LocalDate enddate) {
		this.endDate = enddate;
	}
	@JsonConverter(LocalDateConverter.class)
	public LocalDate getCurrentOverDueDate() {
		return currentOverDueDate;
	}	
	
	@JsonConverter(LocalDateConverter.class)
	public void setCurrentOverDueDate(LocalDate currentoverduedate) {
		this.currentOverDueDate = currentoverduedate;
	}
	public int getRePaymentDays() {
		return rePaymentDays;
	}	
	
	public void setRePaymentDays(int repaymentdays) {
		this.rePaymentDays = repaymentdays;
	}
	public float getRepaymentAmount() {
		return repaymentAmount;
	}	
	
	public void setRepaymentAmount(float repaymentamount) {
		this.repaymentAmount = repaymentamount;
	}
	
	/* all functions for reference*/
	@JsonIgnore
	public LoanRequest getReferedLoanRequest() {
		if (ReferedLoanRequest == null) {
			if (ReferedLoanRequestPK instanceof Long)
				ReferedLoanRequestPK = Math.toIntExact((long) ReferedLoanRequestPK);
			ReferedLoanRequest = EntityManager.getLoanRequestByPK(ReferedLoanRequestPK);
		}
		return ReferedLoanRequest;
	}	
	
	public void setReferedLoanRequest(LoanRequest loanrequest) {
		this.ReferedLoanRequest = loanrequest;
		this.ReferedLoanRequestPK = loanrequest.getPK();
	}			
	@JsonIgnore
	public LoanAccount getBelongedLoanAccount() {
		if (BelongedLoanAccount == null)
			BelongedLoanAccount = EntityManager.getLoanAccountByPK(BelongedLoanAccountPK);
		return BelongedLoanAccount;
	}	
	
	public void setBelongedLoanAccount(LoanAccount loanaccount) {
		this.BelongedLoanAccount = loanaccount;
		this.BelongedLoanAccountPK = loanaccount.getPK();
	}			
	

	/* invarints checking*/
	public boolean Loan_UniqueLoanID() {
		
		if (StandardOPs.isUnique(((List<Loan>)EntityManager.getAllInstancesOf("Loan")), "LoanID")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean Loan_RemainAmountToPayGreatAndEqualZero() {
		
		if (remainAmountToPay >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean Loan_RepaymentAmountGreatAndEqualZero() {
		
		if (repaymentAmount >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean Loan_RePaymentDaysGreatAndEqualZero() {
		
		if (rePaymentDays >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (Loan_UniqueLoanID() && Loan_RemainAmountToPayGreatAndEqualZero() && Loan_RepaymentAmountGreatAndEqualZero() && Loan_RePaymentDaysGreatAndEqualZero()) {
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
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("Loan_UniqueLoanID","Loan_RemainAmountToPayGreatAndEqualZero","Loan_RepaymentAmountGreatAndEqualZero","Loan_RePaymentDaysGreatAndEqualZero"));

}
