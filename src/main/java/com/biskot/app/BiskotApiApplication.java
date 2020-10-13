package com.biskot.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.biskot")
public class BiskotApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiskotApiApplication.class, args);
	}

}
