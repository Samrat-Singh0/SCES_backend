package com.example.mainBase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(scanBasePackages = {
    "com.example.mainBase",
    "com.example",
})
@EnableMethodSecurity
@EnableWebSecurity
public class MainBaseApplication {

  public static void main(String[] args) {
    SpringApplication.run(MainBaseApplication.class, args);

  }
}
