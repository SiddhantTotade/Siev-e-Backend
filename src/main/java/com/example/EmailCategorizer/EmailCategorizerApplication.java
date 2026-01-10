package com.example.EmailCategorizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EmailCategorizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailCategorizerApplication.class, args);
	}

}
