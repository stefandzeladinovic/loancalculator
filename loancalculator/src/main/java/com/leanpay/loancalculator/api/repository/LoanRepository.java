package com.leanpay.loancalculator.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.leanpay.loancalculator.api.model.Loan;

public interface LoanRepository extends CrudRepository<Loan, Long> {

}
