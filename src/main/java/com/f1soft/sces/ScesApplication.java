package com.f1soft.sces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableMethodSecurity
@EnableWebSecurity
public class ScesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScesApplication.class, args);

//		for(int i = 0 ; i < 5 ;i++){
//			System.out.println("PWP-" + UUID.randomUUID());
//		}
	}

}
