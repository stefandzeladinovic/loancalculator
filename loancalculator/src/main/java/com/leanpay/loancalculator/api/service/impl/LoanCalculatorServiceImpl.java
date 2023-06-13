package com.leanpay.loancalculator.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leanpay.loancalculator.api.model.Loan;
import com.leanpay.loancalculator.api.model.LoanMonthly;
import com.leanpay.loancalculator.api.repository.LoanRepository;
import com.leanpay.loancalculator.api.service.LoanCalculatorService;

@Service
public class LoanCalculatorServiceImpl implements LoanCalculatorService {
	
	private static final Logger log = LoggerFactory.getLogger(LoanCalculatorServiceImpl.class);
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Override
	public Loan calculateAndSaveSimpleLoanData(double loanAmount, double annualInterestPercent, int numberOfMonths) {
		/*** 1.Algorithm Simple Loan Calculator: calculate totalAmountPaidWithInterest, monthlyPayment and totalInterestPaid ***/
		double interestRatePerMonth = annualInterestPercent / 100.0 / 12.0;
		double monthlyPaymentAmount = loanAmount * interestRatePerMonth * Math.pow(1 + interestRatePerMonth, numberOfMonths)
				/ (Math.pow(1 + interestRatePerMonth, numberOfMonths) - 1.0);
		double totalAmountPaidWithInterest = monthlyPaymentAmount * numberOfMonths;
		double totalInterestPaid = totalAmountPaidWithInterest - loanAmount;
		Loan loan = new Loan();
		loan.setMonthlyPaymentAmount(monthlyPaymentAmount);
		loan.setTotalInterestPaid(totalInterestPaid);
		loan.setAnnualInterestPercent(annualInterestPercent);
		loan.setInterestRatePerMonth(interestRatePerMonth);
		loan.setLoanAmount(loanAmount);
		loan.setNumberOfMonths(numberOfMonths);
		loan.setTotalAmountPaidWithInterest(totalAmountPaidWithInterest);
		
		printInputParameters(loanAmount, annualInterestPercent, numberOfMonths);
		log.debug("Calculating...");
		printSimpleLoanData(totalAmountPaidWithInterest, interestRatePerMonth, monthlyPaymentAmount, totalInterestPaid);
		
		//save loan to db
		saveLoan(loan);
		
		return loan;
	}

	@Override
	public Loan calculateAndSaveAdvancedLoanData(double loanAmount, double annualInterestPercent, int numberOfMonths) {
		/*** 2.Algorithm Advanced Loan Calculator: calculate amortization loan schedule for all months ***/
		double interestRatePerMonth = annualInterestPercent / 100.0 / 12.0;
		double monthlyPaymentAmount = loanAmount * interestRatePerMonth * Math.pow(1 + interestRatePerMonth, numberOfMonths)
				/ (Math.pow(1 + interestRatePerMonth, numberOfMonths) - 1.0);
		double totalAmountPaidWithInterest = monthlyPaymentAmount * numberOfMonths;
		double totalInterestPaid = totalAmountPaidWithInterest - loanAmount;
		double monthInterestAmount;
		double monthPrincipalAmount;
		double monthBalanceOwed;
		
		//create loan with additional monthly data
		Loan loan = new Loan();
		loan.setLoanAmount(loanAmount);
		loan.setAnnualInterestPercent(annualInterestPercent);
		loan.setTotalAmountPaidWithInterest(totalAmountPaidWithInterest);
		loan.setNumberOfMonths(numberOfMonths);
		loan.setInterestRatePerMonth(interestRatePerMonth);
		loan.setMonthlyPaymentAmount(monthlyPaymentAmount);
		loan.setTotalInterestPaid(totalInterestPaid);
		
		List<LoanMonthly> monthlyLoans = new ArrayList<>(); 
		
		//calculate for each month independently
		log.debug("Calculating amortization schedule for loan...");
		for(int i = 1; i <= numberOfMonths; i++) {
			monthInterestAmount = loanAmount * interestRatePerMonth;
			monthPrincipalAmount = monthlyPaymentAmount - monthInterestAmount;
			//do the correction factor for last month only because of error aggregation
			monthBalanceOwed = (i < numberOfMonths) ? loanAmount - monthPrincipalAmount : 0.0;
			
			//create Monthly loan data
			LoanMonthly loanMonthly = new LoanMonthly();
			loanMonthly.setMonthBalanceOwed(monthBalanceOwed);
			loanMonthly.setMonthPrincipalAmount(monthPrincipalAmount);
			loanMonthly.setMonthInterestAmount(monthInterestAmount);
			
			monthlyLoans.add(loanMonthly);
			
			printMonthlyLoanData(monthlyPaymentAmount, monthPrincipalAmount, monthInterestAmount, monthBalanceOwed, i);
			//for next month decrease value
			loanAmount = monthBalanceOwed;
		}
		
		loan.setMonthlyLoans(monthlyLoans);
		
		//save loan to db
		saveLoan(loan);
		
		return loan;
	}
	
	@Override
	public Iterable<Loan> readAllLoans() {
		log.debug("Returning all loans data from db...");
		return loanRepository.findAll();
	}


	@Override
	public Optional<Loan> readLoanById(Long id) {
		Optional<Loan> loan = loanRepository.findById(id);
		if(loan.isPresent()) {
			log.debug("Loan with id=" + id + " is found. Returning loan data...");
		} else {
			log.debug("Loan with id=" + id + " not found.");
		}
		return loan;
	}


	@Override
	public void deleteAllLoans() {
		loanRepository.deleteAll();
		log.debug("All loans data successfully deleted from db.");
	}


	@Override
	public void deleteLoanById(Long id) {
		loanRepository.deleteById(id);
		log.debug("Loan with id=" + id + " has been successfully deleted from db.");
	}
	
	@Override
	public long getLoansCount() {
		return loanRepository.count();
	}
	
	private Loan saveLoan(Loan loan) {
		Loan savedLoan = loanRepository.save(loan);
		log.debug("Loan data successfully saved to db.");
		return savedLoan;
	}
	
	private void printInputParameters(double loanAmount, double annualInterestPercent, int numberOfMonths) {
		log.debug("Input parameters: loanAmount=" + loanAmount + " annualInterestPercent=" + annualInterestPercent + " numberOfMonths=" + numberOfMonths);
	}
	
	private void printSimpleLoanData(double totalAmountPaidWithInterest, double interestRatePerMonth, double monthlyPayment, double totalInterestPaid) {
		log.debug("Output parameters: totalAmountPaidWithInterest=" + totalAmountPaidWithInterest + " interestRatePerMonth= " + interestRatePerMonth);
		log.debug("SimpleLoanData ----> monthlyPayment=" + monthlyPayment + " totalInterestPaid=" + totalInterestPaid);
	}
	
	private void printMonthlyLoanData(double monthPaymentAmount, double monthPrincipalAmount, double monthInterestAmount, double monthBalanceOwed, int monthOrderNumber) {
		log.debug(monthOrderNumber + ".Month:  PaymentAmount= " + monthPaymentAmount + " PrincipalAmount=" + monthPrincipalAmount + " InterestAmount="
				+ monthInterestAmount + " BalanceOwed=" + monthBalanceOwed);
	}

}
