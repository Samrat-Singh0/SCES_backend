package com.example.mainBase;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordGenerator {

  public static void main(String[] args) {
    String password = "laxman";
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//    System.out.println(encoder.encode(password));

    LocalDateTime now = LocalDateTime.now();
    System.out.println(now);
    System.out.println(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
  }
}
