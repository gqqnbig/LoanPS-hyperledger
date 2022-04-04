package services;

import entities.*;
import java.util.List;
import java.time.LocalDate;


public interface LoanProcessingSystemSystem {

	/* all system operations of the use case*/
	boolean bookNewLoan(int requestid, int loanid, int accountid, LocalDate startdate, LocalDate enddate, int repaymentdays) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean generateStandardPaymentNotice() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean generateLateNotice() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean listBookedLoans() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean loanPayment(int loanid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean closeOutLoan(int loanid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	
	/* invariant checking function */
}
