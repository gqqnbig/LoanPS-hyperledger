package services;

import entities.*;
import java.util.List;
import java.time.LocalDate;


public interface EnterValidatedCreditReferencesModule {

	/* all system operations of the use case*/
	List<LoanRequest> listSubmitedLoanRequest() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	LoanRequest chooseLoanRequest(int requestid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean markRequestValid() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	LoanRequest getCurrentLoanRequest();
	void setCurrentLoanRequest(LoanRequest currentloanrequest);
	List<LoanRequest> getCurrentLoanRequests();
	void setCurrentLoanRequests(List<LoanRequest> currentloanrequests);
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
