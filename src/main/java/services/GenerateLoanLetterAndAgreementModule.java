package services;

import entities.*;
import java.util.List;
import java.time.LocalDate;


public interface GenerateLoanLetterAndAgreementModule {

	/* all system operations of the use case*/
	List<LoanRequest> listApprovalRequest() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean genereateApprovalLetter(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean emailToAppliant() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean generateLoanAgreement() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean printLoanAgreement(int number) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	ApprovalLetter getCurrentApprovalLetter();
	void setCurrentApprovalLetter(ApprovalLetter currentapprovalletter);
	LoanAgreement getCurrentLoanAgreement();
	void setCurrentLoanAgreement(LoanAgreement currentloanagreement);
	LoanRequest getCurrentLoanRequest();
	void setCurrentLoanRequest(LoanRequest currentloanrequest);
	List<LoanRequest> getCurrentLoanRequests();
	void setCurrentLoanRequests(List<LoanRequest> currentloanrequests);
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
