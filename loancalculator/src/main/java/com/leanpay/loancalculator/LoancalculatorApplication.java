package com.leanpay.loancalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoancalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoancalculatorApplication.class, args);
		System.out.println("Loan Calculator here");
	}

}
