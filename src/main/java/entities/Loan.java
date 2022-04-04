package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class Loan implements Serializable {
	
	/* all primary attributes */
	private int LoanID;
	private float RemainAmountToPay;
	private LoanStatus Status;
	private boolean IsPaidinFull;
	private LocalDate StartDate;
	private LocalDate EndDate;
	private LocalDate CurrentOverDueDate;
	private int RePaymentDays;
	private float RepaymentAmount;
	
	/* all references */
	private LoanRequest ReferedLoanRequest; 
	private LoanAccount BelongedLoanAccount; 
	
	/* all get and set functions */
	public int getLoanID() {
		return LoanID;
	}	
	
	public void setLoanID(int loanid) {
		this.LoanID = loanid;
	}
	public float getRemainAmountToPay() {
		return RemainAmountToPay;
	}	
	
	public void setRemainAmountToPay(float remainamounttopay) {
		this.RemainAmountToPay = remainamounttopay;
	}
	public LoanStatus getStatus() {
		return Status;
	}	
	
	public void setStatus(LoanStatus status) {
		this.Status = status;
	}
	public boolean getIsPaidinFull() {
		return IsPaidinFull;
	}	
	
	public void setIsPaidinFull(boolean ispaidinfull) {
		this.IsPaidinFull = ispaidinfull;
	}
	public LocalDate getStartDate() {
		return StartDate;
	}	
	
	public void setStartDate(LocalDate startdate) {
		this.StartDate = startdate;
	}
	public LocalDate getEndDate() {
		return EndDate;
	}	
	
	public void setEndDate(LocalDate enddate) {
		this.EndDate = enddate;
	}
	public LocalDate getCurrentOverDueDate() {
		return CurrentOverDueDate;
	}	
	
	public void setCurrentOverDueDate(LocalDate currentoverduedate) {
		this.CurrentOverDueDate = currentoverduedate;
	}
	public int getRePaymentDays() {
		return RePaymentDays;
	}	
	
	public void setRePaymentDays(int repaymentdays) {
		this.RePaymentDays = repaymentdays;
	}
	public float getRepaymentAmount() {
		return RepaymentAmount;
	}	
	
	public void setRepaymentAmount(float repaymentamount) {
		this.RepaymentAmount = repaymentamount;
	}
	
	/* all functions for reference*/
	public LoanRequest getReferedLoanRequest() {
		return ReferedLoanRequest;
	}	
	
	public void setReferedLoanRequest(LoanRequest loanrequest) {
		this.ReferedLoanRequest = loanrequest;
	}			
	public LoanAccount getBelongedLoanAccount() {
		return BelongedLoanAccount;
	}	
	
	public void setBelongedLoanAccount(LoanAccount loanaccount) {
		this.BelongedLoanAccount = loanaccount;
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
		
		if (RemainAmountToPay >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean Loan_RepaymentAmountGreatAndEqualZero() {
		
		if (RepaymentAmount >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean Loan_RePaymentDaysGreatAndEqualZero() {
		
		if (RePaymentDays >= 0) {
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
