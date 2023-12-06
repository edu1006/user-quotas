package com.vicarius.quotas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestUserQuotasApplication {

	public static void main(String[] args) {
		SpringApplication.from(UserQuotasApplication::main).with(TestUserQuotasApplication.class).run(args);
	}

}
