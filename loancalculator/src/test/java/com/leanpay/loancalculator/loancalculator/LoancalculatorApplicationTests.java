package com.leanpay.loancalculator.loancalculator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.leanpay.loancalculator.api.controller.LoanCalculatorController;

/**
 * Test if application loads correctly Spring boot test
 * @author stdz2709
 *
 */
@SpringBootTest
class LoancalculatorApplicationTests {
	
	@Autowired
	LoanCalculatorController loanCalculatorController;

	@Test
	void contextLoads() {
		Assertions.assertThat(loanCalculatorController).isNotNull();
	}

}
