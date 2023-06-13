package com.leanpay.loancalculator.api.repository.impl;

import org.springframework.stereotype.Repository;

import com.leanpay.loancalculator.api.repository.LoanRepository;

@Repository
public abstract class LoanRepositoryImpl implements LoanRepository {
	/*
	 * Reason I created this class for future use
	 * because one to many relation can add latency in load times
	 * in case more tables not just Loan (one) <----> (many)LoanMonthly
	 * add logic here to fetch just necessary tables by writing custom logic query joins
	 * Use JOIN FETCH approach
	 * Define a @Query method using join fetch and use that instead of predefined find() method. In that way
	 * you can ask Hibernate what other tables to load whenever you are asking to load the parent entity.
	 * 
	 * 
	 * @Query(value = "SELECT l FROM Loan l JOIN FETCH l.monthlyLoans")
	 * List<LoanMonthly> findLoansWithAdvancedCalculation();
	 */
}
