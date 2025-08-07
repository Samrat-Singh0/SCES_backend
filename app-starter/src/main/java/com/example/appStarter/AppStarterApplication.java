package com.example.appStarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
    "com.example",
    "com.example.appStarter",
    "com.example.mainBase"
})
@EnableJpaRepositories({"com.example.mainBase"})
@EntityScan("com.example.mainBase")
public class AppStarterApplication {

  public static void main(String[] args) {
    SpringApplication.run(AppStarterApplication.class, args);
  }

}
