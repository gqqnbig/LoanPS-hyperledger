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
public class CheckingAccount implements Serializable {
	
	/* all primary attributes */
	@Property()
	private float balance;
	@Property()
	private CheckingAccountStatus status;
	
	/* all references */
	
	/* all get and set functions */
	public float getBalance() {
		return balance;
	}	
	
	public void setBalance(float balance) {
		this.balance = balance;
	}
	public CheckingAccountStatus getStatus() {
		return status;
	}	
	
	public void setStatus(CheckingAccountStatus status) {
		this.status = status;
	}
	
	/* all functions for reference*/
	

	/* invarints checking*/
	public boolean CheckingAccount_BalanceGreatAndEqualZero() {
		
		if (balance >= 0) {
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
