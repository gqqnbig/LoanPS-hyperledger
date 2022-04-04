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
public class LoanTerm implements Serializable {
	public Object getPK() {
		return getItemID();
	}
	
	/* all primary attributes */
	@Property()
	private int itemID;
	@Property()
	private String content;
	
	/* all references */
	
	/* all get and set functions */
	public int getItemID() {
		return itemID;
	}	
	
	public void setItemID(int itemid) {
		this.itemID = itemid;
	}
	public String getContent() {
		return content;
	}	
	
	public void setContent(String content) {
		this.content = content;
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
