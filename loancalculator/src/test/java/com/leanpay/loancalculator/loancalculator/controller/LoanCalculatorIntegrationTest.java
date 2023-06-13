package com.leanpay.loancalculator.loancalculator.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.leanpay.loancalculator.api.controller.LoanCalculatorController;
import com.leanpay.loancalculator.api.model.Loan;
import com.leanpay.loancalculator.api.model.LoanMonthly;

/**
 * Integration test for loan calculator:
 * call controller with parameters to make calculation and compare results with exact ones
 * then do controller read from DB check if it is saved and compare with data sent
 * if results are the same OK
 * Then delete data used for test in the end
 * @author stdz2709
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LoanCalculatorIntegrationTest {
	
	@Autowired
	LoanCalculatorController loanCalculatorController;
	
	@Test
	public void calculateAndSaveSimpleLoanDataIntegrationTest() {
		//check if calculation is OK
		Loan loan = new Loan();
		//setup object with correct data calculation
		loan.setAnnualInterestPercent(5.0);
		loan.setInterestRatePerMonth(0.004166666666666667);
		loan.setLoanAmount(20000.0);
		loan.setMonthlyPaymentAmount(377.42467288021976);
		loan.setNumberOfMonths(60);
		loan.setTotalAmountPaidWithInterest(22645.480372813185);
		loan.setTotalInterestPaid(2645.4803728131847);
		
		Loan loanReturnedByController = loanCalculatorController.calculateSimpleLoanAndSave("20000.0", "5.0", "60");
		
		assertEquals(loan.getAnnualInterestPercent(), loanReturnedByController.getAnnualInterestPercent());
		assertEquals(loan.getInterestRatePerMonth(), loanReturnedByController.getInterestRatePerMonth());
		assertEquals(loan.getLoanAmount(), loanReturnedByController.getLoanAmount());
		assertEquals(loan.getMonthlyPaymentAmount(), loanReturnedByController.getMonthlyPaymentAmount());
		assertEquals(loan.getNumberOfMonths(), loanReturnedByController.getNumberOfMonths());
		assertEquals(loan.getTotalAmountPaidWithInterest(), loanReturnedByController.getTotalAmountPaidWithInterest());
		assertEquals(loan.getTotalInterestPaid(), loanReturnedByController.getTotalInterestPaid());
		assertTrue(loan.getMonthlyLoans().size() == 0);
		assertTrue(loanReturnedByController.getMonthlyLoans().size() == 0);
		
		//check if data is saved to DB
		Optional<Loan> loanReturnedFromDBOptional = loanCalculatorController.readLoanById(loanReturnedByController.getId());
		assertTrue(loanReturnedFromDBOptional.isPresent());
		Loan loanReturnedFromDB = loanReturnedFromDBOptional.get();
		
		
		//check data sent and one from DB are the same
		assertEquals(loan.getAnnualInterestPercent(), loanReturnedFromDB.getAnnualInterestPercent());
		assertEquals(loan.getInterestRatePerMonth(), loanReturnedFromDB.getInterestRatePerMonth());
		assertEquals(loan.getLoanAmount(), loanReturnedFromDB.getLoanAmount());
		assertEquals(loan.getMonthlyPaymentAmount(), loanReturnedFromDB.getMonthlyPaymentAmount());
		assertEquals(loan.getNumberOfMonths(), loanReturnedFromDB.getNumberOfMonths());
		assertEquals(loan.getTotalAmountPaidWithInterest(), loanReturnedFromDB.getTotalAmountPaidWithInterest());
		assertEquals(loan.getTotalInterestPaid(), loanReturnedFromDB.getTotalInterestPaid());
		assertTrue(loan.getMonthlyLoans().size() == 0);
		assertTrue(loanReturnedFromDB.getMonthlyLoans().size() == 0);
		
		//delete data for next test
		loanCalculatorController.deleteLoanById(loanReturnedFromDB.getId());
		Optional<Loan> deletedLoanOptional = loanCalculatorController.readLoanById(loanReturnedFromDB.getId());
		//check if it is deleted
		assertFalse(deletedLoanOptional.isPresent());
	}
	
	@Test
	public void calculateAndSaveAdvancedLoanDataIntegrationTest() {
		//check if calculation is OK
		Loan loan = new Loan();
		//setup object with correct data calculation
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
		
		Loan loanReturnedByController = loanCalculatorController.calculateAdvancedLoanAndSave("20000.0", "5.0", "60");
		
		assertEquals(loan.getAnnualInterestPercent(), loanReturnedByController.getAnnualInterestPercent());
		assertEquals(loan.getInterestRatePerMonth(), loanReturnedByController.getInterestRatePerMonth());
		assertEquals(loan.getLoanAmount(), loanReturnedByController.getLoanAmount());
		assertEquals(loan.getMonthlyPaymentAmount(), loanReturnedByController.getMonthlyPaymentAmount());
		assertEquals(loan.getNumberOfMonths(), loanReturnedByController.getNumberOfMonths());
		assertEquals(loan.getTotalAmountPaidWithInterest(), loanReturnedByController.getTotalAmountPaidWithInterest());
		assertEquals(loan.getTotalInterestPaid(), loanReturnedByController.getTotalInterestPaid());
		assertTrue(loan.getMonthlyLoans().size() > 0);
		assertTrue(loanReturnedByController.getMonthlyLoans().size() > 0);
		
		//check data for first month
		//check first month if it is OK, others are fine
		LoanMonthly monthlyLoanReturnedByController = loanReturnedByController.getMonthlyLoans().get(0);
		assertEquals(firstMonth.getMonthPrincipalAmount(), monthlyLoanReturnedByController.getMonthPrincipalAmount());
		assertEquals(firstMonth.getMonthInterestAmount(), monthlyLoanReturnedByController.getMonthInterestAmount());
		assertEquals(firstMonth.getMonthBalanceOwed(), monthlyLoanReturnedByController.getMonthBalanceOwed());
		
		//check if data is saved to DB
		Optional<Loan> loanReturnedFromDBOptional = loanCalculatorController.readLoanById(loanReturnedByController.getId());
		assertTrue(loanReturnedFromDBOptional.isPresent());
		Loan loanReturnedFromDB = loanReturnedFromDBOptional.get();
		
		
		//check data sent and one from DB are the same
		assertEquals(loan.getAnnualInterestPercent(), loanReturnedFromDB.getAnnualInterestPercent());
		assertEquals(loan.getInterestRatePerMonth(), loanReturnedFromDB.getInterestRatePerMonth());
		assertEquals(loan.getLoanAmount(), loanReturnedFromDB.getLoanAmount());
		assertEquals(loan.getMonthlyPaymentAmount(), loanReturnedFromDB.getMonthlyPaymentAmount());
		assertEquals(loan.getNumberOfMonths(), loanReturnedFromDB.getNumberOfMonths());
		assertEquals(loan.getTotalAmountPaidWithInterest(), loanReturnedFromDB.getTotalAmountPaidWithInterest());
		assertEquals(loan.getTotalInterestPaid(), loanReturnedFromDB.getTotalInterestPaid());
		assertTrue(loan.getMonthlyLoans().size() > 0);
		assertTrue(loanReturnedFromDB.getMonthlyLoans().size() > 0);
		
		//check data for first month
		//check first month if it is OK, others are fine
		LoanMonthly monthlyLoanReturnedFromDB = loanReturnedFromDB.getMonthlyLoans().get(0);
		assertEquals(firstMonth.getMonthPrincipalAmount(), monthlyLoanReturnedFromDB.getMonthPrincipalAmount());
		assertEquals(firstMonth.getMonthInterestAmount(), monthlyLoanReturnedFromDB.getMonthInterestAmount());
		assertEquals(firstMonth.getMonthBalanceOwed(), monthlyLoanReturnedFromDB.getMonthBalanceOwed());
		
		//delete data for next test
		loanCalculatorController.deleteLoanById(loanReturnedFromDB.getId());
		Optional<Loan> deletedLoanOptional = loanCalculatorController.readLoanById(loanReturnedFromDB.getId());
		//check if it is deleted
		assertFalse(deletedLoanOptional.isPresent());
	}
}
