package services;

import entities.*;
import java.util.List;
import java.time.LocalDate;


public interface EvaluateLoanRequestModule {

	/* all system operations of the use case*/
	List<LoanRequest> listTenLoanRequest() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	LoanRequest chooseOneForReview(int requestid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	CreditHistory checkCreditHistory() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	CheckingAccount reviewCheckingAccount() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	List<LoanTerm> listAvaiableLoanTerm() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean addLoanTerm(int termid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean approveLoanRequest() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	LoanRequest getCurrentLoanRequest();
	void setCurrentLoanRequest(LoanRequest currentloanrequest);
	List<LoanRequest> getCurrentLoanRequests();
	void setCurrentLoanRequests(List<LoanRequest> currentloanrequests);
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
