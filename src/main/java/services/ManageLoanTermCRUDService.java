package services;

import entities.*;
import java.util.List;
import java.time.LocalDate;


public interface ManageLoanTermCRUDService {

	/* all system operations of the use case*/
	boolean createLoanTerm(int itemid, String content) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	LoanTerm queryLoanTerm(int itemid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean modifyLoanTerm(int itemid, String content) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean deleteLoanTerm(int itemid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
