package com.example.mainBase.util;

import org.springframework.stereotype.Component;

@Component("securityRoles")
public class SecurityRoles {

  public final String SUPER = "SUPER_ADMIN";
  public final String INSTRUCTOR = "INSTRUCTOR";
  public final String STUDENT = "STUDENT";
}
