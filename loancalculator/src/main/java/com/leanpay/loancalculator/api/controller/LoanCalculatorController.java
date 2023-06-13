package com.leanpay.loancalculator.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.leanpay.loancalculator.api.model.Loan;
import com.leanpay.loancalculator.api.service.LoanCalculatorService;

@RestController
@RequestMapping("/loancalculator")
public class LoanCalculatorController {

    @Autowired
    private LoanCalculatorService loanCalculationService;

    /*
     * Calculates simple loan data and saves data
     */
    @GetMapping("/calculateSimpleLoan")
    public @ResponseBody Loan calculateSimpleLoanAndSave(@RequestParam String loanAmount, @RequestParam String annualInterestPercent, @RequestParam String numberOfMonths) {
    	Loan simpleLoan = loanCalculationService.calculateAndSaveSimpleLoanData(Double.valueOf(loanAmount), Double.valueOf(annualInterestPercent), Integer.valueOf(numberOfMonths));
    	return simpleLoan;
    }
    
    /*
     * Calculates advanced (amortization schedule) loan data and saves data
     */
    @GetMapping("/calculateAdvancedLoan")
    public @ResponseBody Loan calculateAdvancedLoanAndSave(@RequestParam String loanAmount, @RequestParam String annualInterestPercent, @RequestParam String numberOfMonths) {
    	Loan advancedLoan = loanCalculationService.calculateAndSaveAdvancedLoanData(Double.valueOf(loanAmount), Double.valueOf(annualInterestPercent), Integer.valueOf(numberOfMonths));
    	return advancedLoan;
    }
    
    /*
     * Reads all loans data
     */
    @GetMapping("/loans/readAll")
    public @ResponseBody Iterable<Loan> readAllLoans() {
    	return loanCalculationService.readAllLoans();
    }
    
    /*
     * Reads number of loans data (requests) in system
     */
    @GetMapping("/loans/getCount")
    public @ResponseBody Long getLoansCount() {
    	return loanCalculationService.getLoansCount();
    }
    
    /*
     * Reads loan by id
     */
    @GetMapping("/loans/read/{id}")
    public Optional<Loan> readLoanById(@PathVariable Long id) {
        return loanCalculationService.readLoanById(id);
    }
    
    /*
     * Delete all loans data
     */
    @DeleteMapping("/loans/deleteAll")
    public String deleteAllLoans() {
    	loanCalculationService.deleteAllLoans();
        return "All loans data successfully deleted from db.";
    }
    
    /*
     * Deletes loan by id
     */
    @DeleteMapping("/loans/delete/{id}")
    public String deleteLoanById(@PathVariable("id") Long id) {
    	loanCalculationService.deleteLoanById(id);
        return "Loan with id=" + id + " has been successfully deleted from db.";
    }
}
