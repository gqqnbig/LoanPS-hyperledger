package services;

import entities.*;
import java.util.List;
import java.time.LocalDate;


public interface ThirdPartyServices {

	/* all system operations of the use case*/
	boolean sendEmail(String emailaddress, String title, String content) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean print(String content, int numbers) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	LoanAccount createLoanAccount(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean transferFunds(int id, float amount) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	CreditHistory getCreditHistory(int securityid, String name) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	CheckingAccount getCheckingAccountStatus(int cid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
