package com.leanpay.loancalculator.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name="LoanMonthly")
@Entity
public class LoanMonthly {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private double monthPrincipalAmount;
	private double monthInterestAmount;
	private double monthBalanceOwed;
	
	public LoanMonthly() {
	}
	
	public LoanMonthly(double monthPrincipalAmount, double monthInterestAmount,
			double monthBalanceOwed) {
		this.monthPrincipalAmount = monthPrincipalAmount;
		this.monthInterestAmount = monthInterestAmount;
		this.monthBalanceOwed = monthBalanceOwed;
	}
	
	public double getMonthPrincipalAmount() {
		return monthPrincipalAmount;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMonthPrincipalAmount(double monthPrincipalAmount) {
		this.monthPrincipalAmount = monthPrincipalAmount;
	}
	public double getMonthInterestAmount() {
		return monthInterestAmount;
	}
	public void setMonthInterestAmount(double monthInterestAmount) {
		this.monthInterestAmount = monthInterestAmount;
	}
	public double getMonthBalanceOwed() {
		return monthBalanceOwed;
	}
	public void setMonthBalanceOwed(double monthBalanceOwed) {
		this.monthBalanceOwed = monthBalanceOwed;
	}
}
