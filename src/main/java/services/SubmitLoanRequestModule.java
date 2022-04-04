package services;

import entities.*;
import java.util.List;
import java.time.LocalDate;


public interface SubmitLoanRequestModule {

	/* all system operations of the use case*/
	boolean enterLoanInformation(int requestid, String name, float loanamount, String loanpurpose, float income, int phonenumber, String postaladdress, int zipcode, String email, String workreferences, String creditreferences, int checkingaccountnumber, int securitynumber) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean creditRequest() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean accountStatusRequest() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	int calculateScore() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	LoanRequest getCurrentLoanRequest();
	void setCurrentLoanRequest(LoanRequest currentloanrequest);
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
