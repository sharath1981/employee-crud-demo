package com.kpt;

import com.kpt.employee.EmployeeService;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EmployeeCrudDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeCrudDemoApplication.class, args);
	}

	@Bean
	public ApplicationRunner runner(final EmployeeService employeeService) {
		return args -> {
			employeeService.loadInitialEmployees();
		};
	}
}
