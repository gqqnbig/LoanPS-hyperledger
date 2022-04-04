package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class CheckingAccount implements Serializable {
	
	/* all primary attributes */
	private float Balance;
	private CheckingAccountStatus Status;
	
	/* all references */
	
	/* all get and set functions */
	public float getBalance() {
		return Balance;
	}	
	
	public void setBalance(float balance) {
		this.Balance = balance;
	}
	public CheckingAccountStatus getStatus() {
		return Status;
	}	
	
	public void setStatus(CheckingAccountStatus status) {
		this.Status = status;
	}
	
	/* all functions for reference*/
	

	/* invarints checking*/
	public boolean CheckingAccount_BalanceGreatAndEqualZero() {
		
		if (Balance >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (CheckingAccount_BalanceGreatAndEqualZero()) {
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
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("CheckingAccount_BalanceGreatAndEqualZero"));

}
