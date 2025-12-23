package com.java.javaproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JavaprojectApplication {

	public static void main(String[] args) {
		System.out.println("Scheduler Started Running");
		SpringApplication.run(JavaprojectApplication.class, args);
	}

}
