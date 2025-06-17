package com.f1soft.sces;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptHashGenerator {

  public static void main(String[] args) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String rawPassword = "smriti@123";
    String encodedPassword = encoder.encode(rawPassword);
    System.out.println(encodedPassword);
  }
}
