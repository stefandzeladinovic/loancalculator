package com.leanpay.loancalculator.loancalculator.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;
import com.leanpay.loancalculator.api.controller.LoanCalculatorController;
import com.leanpay.loancalculator.api.model.Loan;
import com.leanpay.loancalculator.api.model.LoanMonthly;
import com.leanpay.loancalculator.api.repository.LoanRepository;
import com.leanpay.loancalculator.api.service.LoanCalculatorService;

import net.minidev.json.JSONArray;

/**
 * System testing
 * Unit test for controller
 * By stubbing heavy objects and do mock of the service and repository layer
 * @author stdz2709
 *
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(LoanCalculatorController.class)
public class StandaloneLoanCalculatorControllerTest {
	
	@MockBean
	LoanCalculatorService loanCalculatorService;
	@MockBean
	LoanRepository loanRepository;

	@Autowired
	MockMvc mockMvc;
	
	/* System Tests are just for calculateSimpleLoanAndSave and calculateAdvancedLoanAndSave, other methods rely
	 * on crudeRepository interface which is unchanged(tested from Framework)
	 * System test tests cover user use case scenario
	 * */
	@Test
	public void testCalculateSimpleLoanAndSaveController() throws Exception {
		Loan loan = new Loan();
		//setup dummy object
		loan.setAnnualInterestPercent(5.0);
		loan.setInterestRatePerMonth(0.004166666666666667);
		loan.setLoanAmount(20000.0);
		loan.setMonthlyPaymentAmount(377.42467288021976);
		loan.setNumberOfMonths(60);
		loan.setTotalAmountPaidWithInterest(22645.480372813185);
		loan.setTotalInterestPaid(2645.4803728131847);

		Mockito.when(loanCalculatorService.calculateAndSaveSimpleLoanData(20000.0, 5.0, 60)).thenReturn(loan);

		MvcResult httpResult = mockMvc.perform(get("/loancalculator/calculateSimpleLoan?loanAmount=20000&annualInterestPercent=5&numberOfMonths=60"))
				.andExpect(status().isOk()).andReturn();
		
		String jsonResponse = httpResult.getResponse().getContentAsString();
		
		double loanAmount = JsonPath.read(jsonResponse, "$.loanAmount");
		Assertions.assertEquals(20000.0, loanAmount);
		double annualInterestPercent = JsonPath.read(jsonResponse, "$.annualInterestPercent");
		Assertions.assertEquals(5.0, annualInterestPercent);
		int numberOfMonths = JsonPath.read(jsonResponse, "$.numberOfMonths");
		Assertions.assertEquals(60, numberOfMonths);
		double totalAmountPaidWithInterest = JsonPath.read(jsonResponse, "$.totalAmountPaidWithInterest");
		Assertions.assertEquals(22645.480372813185, totalAmountPaidWithInterest);
		double monthlyPaymentAmount = JsonPath.read(jsonResponse, "$.monthlyPaymentAmount");
		Assertions.assertEquals(377.42467288021976, monthlyPaymentAmount);
		double totalInterestPaid = JsonPath.read(jsonResponse, "$.totalInterestPaid");
		Assertions.assertEquals(2645.4803728131847, totalInterestPaid);
		BigDecimal interestRatePerMonth = JsonPath.read(jsonResponse, "$.interestRatePerMonth");
		Assertions.assertEquals(0.004166666666666667, interestRatePerMonth.doubleValue());
		
		//check if monthlyLoans array is empty (simple calculator test)
		JSONArray monthlyLoans = JsonPath.read(jsonResponse, "$.monthlyLoans");
		assertTrue(monthlyLoans.size() < 1);
		
	}
	
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

		Mockito.when(loanCalculatorService.calculateAndSaveAdvancedLoanData(20000.0, 5.0, 60)).thenReturn(loan);

		MvcResult httpResult = mockMvc.perform(get("/loancalculator/calculateAdvancedLoan?loanAmount=20000&annualInterestPercent=5&numberOfMonths=60"))
				.andExpect(status().isOk()).andReturn();
		
		String jsonResponse = httpResult.getResponse().getContentAsString();
		
		double loanAmount = JsonPath.read(jsonResponse, "$.loanAmount");
		Assertions.assertEquals(20000.0, loanAmount);
		double annualInterestPercent = JsonPath.read(jsonResponse, "$.annualInterestPercent");
		Assertions.assertEquals(5.0, annualInterestPercent);
		int numberOfMonths = JsonPath.read(jsonResponse, "$.numberOfMonths");
		Assertions.assertEquals(60, numberOfMonths);
		double totalAmountPaidWithInterest = JsonPath.read(jsonResponse, "$.totalAmountPaidWithInterest");
		Assertions.assertEquals(22645.480372813185, totalAmountPaidWithInterest);
		double monthlyPaymentAmount = JsonPath.read(jsonResponse, "$.monthlyPaymentAmount");
		Assertions.assertEquals(377.42467288021976, monthlyPaymentAmount);
		double totalInterestPaid = JsonPath.read(jsonResponse, "$.totalInterestPaid");
		Assertions.assertEquals(2645.4803728131847, totalInterestPaid);
		BigDecimal interestRatePerMonth = JsonPath.read(jsonResponse, "$.interestRatePerMonth");
		Assertions.assertEquals(0.004166666666666667, interestRatePerMonth.doubleValue());
		
		//check if monthlyLoans array is not empty (simple calculator test)
		JSONArray monthlyLoans = JsonPath.read(jsonResponse, "$.monthlyLoans");
		assertTrue(monthlyLoans.size() > 0);
		
		//check first month
		double monthPrincipalAmount = JsonPath.read(jsonResponse, "$.monthlyLoans[0].monthPrincipalAmount");
		Assertions.assertEquals(294.09133954688645, monthPrincipalAmount);
		double monthInterestAmount = JsonPath.read(jsonResponse, "$.monthlyLoans[0].monthInterestAmount");
		Assertions.assertEquals(83.33333333333333, monthInterestAmount);
		double monthBalanceOwed = JsonPath.read(jsonResponse, "$.monthlyLoans[0].monthBalanceOwed");
		Assertions.assertEquals(19705.908660453115, monthBalanceOwed);
	}
}
