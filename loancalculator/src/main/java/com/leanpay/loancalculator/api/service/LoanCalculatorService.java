package com.leanpay.loancalculator.api.service;

import java.util.Optional;

import com.leanpay.loancalculator.api.model.Loan;

public interface LoanCalculatorService {
	
	//Methods for loan calculation and DB save read and delete operations
	public Loan calculateAndSaveSimpleLoanData(double loanAmount, double annualInterestPercent, int numberOfMonths);
	public Loan calculateAndSaveAdvancedLoanData(double loanAmount, double annualInterestPercent, int numberOfMonths);
	public Iterable<Loan> readAllLoans();
	public Optional<Loan> readLoanById(Long id);
	public void deleteAllLoans();
	public void deleteLoanById(Long id);
	public long getLoansCount();
	
}
