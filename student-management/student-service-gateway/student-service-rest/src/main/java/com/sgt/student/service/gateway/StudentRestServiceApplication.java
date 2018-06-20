package com.sgt.student.service.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The Student Rest Service Spring Boot application
 */

@SpringBootApplication
@ComponentScan({"com.sgt.student.service.client.**","com.sgt.student.service.gateway","com.sgt.student.service.gateway.controller"})
public class StudentRestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentRestServiceApplication.class, args);
	}
}
