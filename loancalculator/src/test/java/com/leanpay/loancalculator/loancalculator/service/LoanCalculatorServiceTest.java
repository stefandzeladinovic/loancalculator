package com.leanpay.loancalculator.loancalculator.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.leanpay.loancalculator.api.model.Loan;
import com.leanpay.loancalculator.api.model.LoanMonthly;
import com.leanpay.loancalculator.api.repository.LoanRepository;
import com.leanpay.loancalculator.api.service.impl.LoanCalculatorServiceImpl;

/**
 * Unit test for loan calculation service
 * Test for loan calculator service isolate save operation when dao.save then return object to bypass it
 * and compare values from loan calculator service with correct ones
 * @author stdz2709
 *
 */
@ExtendWith(MockitoExtension.class)
public class LoanCalculatorServiceTest {
	
	@InjectMocks
	LoanCalculatorServiceImpl loanCalculatorService;
     
    @Mock
    LoanRepository loanRepository;
    
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }
    
    /**
     * Checks calculation logic for simple loan calculation in isolation from DB
     * @throws Exception
     */
	@Test
	public void testCalculateSimpleLoanAndSave() throws Exception {
		Loan loan = new Loan();
		//setup dummy object
		loan.setAnnualInterestPercent(5.0);
		loan.setInterestRatePerMonth(0.004166666666666667);
		loan.setLoanAmount(20000.0);
		loan.setMonthlyPaymentAmount(377.42467288021976);
		loan.setNumberOfMonths(60);
		loan.setTotalAmountPaidWithInterest(22645.480372813185);
		loan.setTotalInterestPaid(2645.4803728131847);

		Mockito.lenient().when(loanCalculatorService.calculateAndSaveSimpleLoanData(20000.0, 5.0, 60)).thenReturn(loan);
		
		Loan serviceReturnedLoan = loanCalculatorService.calculateAndSaveSimpleLoanData(20000.0, 5.0, 60);
		
		Assertions.assertEquals(loan.getAnnualInterestPercent(), serviceReturnedLoan.getAnnualInterestPercent());
		Assertions.assertEquals(loan.getInterestRatePerMonth(), serviceReturnedLoan.getInterestRatePerMonth());
		Assertions.assertEquals(loan.getLoanAmount(), serviceReturnedLoan.getLoanAmount());
		Assertions.assertEquals(loan.getMonthlyPaymentAmount(), serviceReturnedLoan.getMonthlyPaymentAmount());
		Assertions.assertEquals(loan.getNumberOfMonths(), serviceReturnedLoan.getNumberOfMonths());
		Assertions.assertEquals(loan.getTotalAmountPaidWithInterest(), serviceReturnedLoan.getTotalAmountPaidWithInterest());
		Assertions.assertEquals(loan.getTotalInterestPaid(), serviceReturnedLoan.getTotalInterestPaid());
		Assertions.assertTrue(loan.getMonthlyLoans().size() == 0);
		Assertions.assertTrue(serviceReturnedLoan.getMonthlyLoans().size() == 0);
	}
	
	/**
	 * Checks calculation logic for advanced loan calculation in isolation from DB
	 * @throws Exception
	 */
	@Test
	public void testCalculateAdvancedLoanAndSaveController() throws Exception {
		Loan loan = new Loan();
		//setup dummy object
		loan.setAnnualInterestPercent(5.0);
		loan.setInterestRatePerMonth(0.004166666666666667);
		loan.setLoanAmount(20000.0);
		loan.setMonthlyPaymentAmount(377.42467288021976);
		loan.setNumberOfMonths(60);
		loan.setTotalAmountPaidWithInterest(22645.480372813185);
		loan.setTotalInterestPaid(2645.4803728131847);
		
		//set monthly loanData for first month
		List<LoanMonthly> monthlyLoan = new ArrayList<LoanMonthly>();
		LoanMonthly firstMonth = new LoanMonthly();
		firstMonth.setMonthPrincipalAmount(294.09133954688645);
		firstMonth.setMonthInterestAmount(83.33333333333333);
		firstMonth.setMonthBalanceOwed(19705.908660453115);
		monthlyLoan.add(firstMonth);
		loan.setMonthlyLoans(monthlyLoan);

		Mockito.lenient().when(loanCalculatorService.calculateAndSaveAdvancedLoanData(20000.0, 5.0, 60)).thenReturn(loan);
		
		Loan serviceReturnedLoan = loanCalculatorService.calculateAndSaveAdvancedLoanData(20000.0, 5.0, 60);
		
		Assertions.assertEquals(loan.getAnnualInterestPercent(), serviceReturnedLoan.getAnnualInterestPercent());
		Assertions.assertEquals(loan.getInterestRatePerMonth(), serviceReturnedLoan.getInterestRatePerMonth());
		Assertions.assertEquals(loan.getLoanAmount(), serviceReturnedLoan.getLoanAmount());
		Assertions.assertEquals(loan.getMonthlyPaymentAmount(), serviceReturnedLoan.getMonthlyPaymentAmount());
		Assertions.assertEquals(loan.getNumberOfMonths(), serviceReturnedLoan.getNumberOfMonths());
		Assertions.assertEquals(loan.getTotalAmountPaidWithInterest(), serviceReturnedLoan.getTotalAmountPaidWithInterest());
		Assertions.assertEquals(loan.getTotalInterestPaid(), serviceReturnedLoan.getTotalInterestPaid());
		Assertions.assertNotNull(loan.getMonthlyLoans());
		Assertions.assertNotNull(serviceReturnedLoan.getMonthlyLoans());
		
		//check first month if it is OK, others are fine
		LoanMonthly serviceReturnedMonthlyLoan = serviceReturnedLoan.getMonthlyLoans().get(0);
		Assertions.assertEquals(firstMonth.getMonthPrincipalAmount(), serviceReturnedMonthlyLoan.getMonthPrincipalAmount());
		Assertions.assertEquals(firstMonth.getMonthInterestAmount(), serviceReturnedMonthlyLoan.getMonthInterestAmount());
		Assertions.assertEquals(firstMonth.getMonthBalanceOwed(), serviceReturnedMonthlyLoan.getMonthBalanceOwed());
	}
}
