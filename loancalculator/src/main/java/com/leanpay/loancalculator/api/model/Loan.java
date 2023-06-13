package com.leanpay.loancalculator.api.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name="Loan")
@Entity
public class Loan {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "lm_fid", referencedColumnName = "id")
	List<LoanMonthly> monthlyLoans = new ArrayList<>();
	
	private double loanAmount;
	private double annualInterestPercent;
	private int numberOfMonths;
	private double totalAmountPaidWithInterest;
	private double interestRatePerMonth;
	private double monthlyPaymentAmount;
	private double totalInterestPaid;
	
	public Loan() {
	}
	
	public Loan(List<LoanMonthly> monthlyLoans, double loanAmount, double annualInterestPercent, int numberOfMonths,
			double totalAmountPaidWithInterest, double interestRatePerMonth, double monthlyPaymentAmount,
			double totalInterestPaid) {
		super();
		this.monthlyLoans = monthlyLoans;
		this.loanAmount = loanAmount;
		this.annualInterestPercent = annualInterestPercent;
		this.numberOfMonths = numberOfMonths;
		this.totalAmountPaidWithInterest = totalAmountPaidWithInterest;
		this.interestRatePerMonth = interestRatePerMonth;
		this.monthlyPaymentAmount = monthlyPaymentAmount;
		this.totalInterestPaid = totalInterestPaid;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<LoanMonthly> getMonthlyLoans() {
		return monthlyLoans;
	}
	public void setMonthlyLoans(List<LoanMonthly> monthlyLoans) {
		this.monthlyLoans = monthlyLoans;
	}
	public double getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}
	public double getAnnualInterestPercent() {
		return annualInterestPercent;
	}
	public void setAnnualInterestPercent(double annualInterestPercent) {
		this.annualInterestPercent = annualInterestPercent;
	}
	public int getNumberOfMonths() {
		return numberOfMonths;
	}
	public void setNumberOfMonths(int numberOfMonths) {
		this.numberOfMonths = numberOfMonths;
	}
	public double getTotalAmountPaidWithInterest() {
		return totalAmountPaidWithInterest;
	}
	public void setTotalAmountPaidWithInterest(double totalAmountPaidWithInterest) {
		this.totalAmountPaidWithInterest = totalAmountPaidWithInterest;
	}
	public double getInterestRatePerMonth() {
		return interestRatePerMonth;
	}
	public void setInterestRatePerMonth(double interestRatePerMonth) {
		this.interestRatePerMonth = interestRatePerMonth;
	}
	public double getMonthlyPaymentAmount() {
		return monthlyPaymentAmount;
	}
	public void setMonthlyPaymentAmount(double monthlyPaymentAmount) {
		this.monthlyPaymentAmount = monthlyPaymentAmount;
	}
	public double getTotalInterestPaid() {
		return totalInterestPaid;
	}
	public void setTotalInterestPaid(double totalInterestPaid) {
		this.totalInterestPaid = totalInterestPaid;
	}
}
