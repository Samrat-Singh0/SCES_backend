package com.example.mainBase.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordGenerator {

  public static void main(String[] args) {
    String password = "aayush@123";
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    System.out.println(encoder.encode(password));

  }
}
