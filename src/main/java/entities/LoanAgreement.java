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
public class LoanAgreement implements Serializable {
	
	/* all primary attributes */
	@Property()
	private String content;
	
	/* all references */
	
	/* all get and set functions */
	public String getContent() {
		return content;
	}	
	
	public void setContent(String content) {
		this.content = content;
	}
	
	/* all functions for reference*/
	


}
