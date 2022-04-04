package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class LoanTerm implements Serializable {
	
	/* all primary attributes */
	private int ItemID;
	private String Content;
	
	/* all references */
	
	/* all get and set functions */
	public int getItemID() {
		return ItemID;
	}	
	
	public void setItemID(int itemid) {
		this.ItemID = itemid;
	}
	public String getContent() {
		return Content;
	}	
	
	public void setContent(String content) {
		this.Content = content;
	}
	
	/* all functions for reference*/
	

	/* invarints checking*/
	public boolean LoanTerm_UniqueLoanID() {
		
		if (StandardOPs.isUnique(((List<Loan>)EntityManager.getAllInstancesOf("Loan")), "LoanID")) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (LoanTerm_UniqueLoanID()) {
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
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("LoanTerm_UniqueLoanID"));

}
