package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class CreditHistory implements Serializable {
	
	/* all primary attributes */
	private float OutstandingDebt;
	private int BadDebits;
	
	/* all references */
	
	/* all get and set functions */
	public float getOutstandingDebt() {
		return OutstandingDebt;
	}	
	
	public void setOutstandingDebt(float outstandingdebt) {
		this.OutstandingDebt = outstandingdebt;
	}
	public int getBadDebits() {
		return BadDebits;
	}	
	
	public void setBadDebits(int baddebits) {
		this.BadDebits = baddebits;
	}
	
	/* all functions for reference*/
	

	/* invarints checking*/
	public boolean CreditHistory_OutstandingDebtGreatAndEqualZero() {
		
		if (OutstandingDebt >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean CreditHistory_BadDebitsGreatAndEqualZero() {
		
		if (BadDebits >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (CreditHistory_OutstandingDebtGreatAndEqualZero() && CreditHistory_BadDebitsGreatAndEqualZero()) {
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
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("CreditHistory_OutstandingDebtGreatAndEqualZero","CreditHistory_BadDebitsGreatAndEqualZero"));

}
