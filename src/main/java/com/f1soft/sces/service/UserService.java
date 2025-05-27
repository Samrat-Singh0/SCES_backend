package com.f1soft.sces.service;

import com.f1soft.sces.models.SignupRequest;
import com.f1soft.sces.entities.User;

public interface UserService {
  User registerUser(SignupRequest signupRequest);
  User findUserByEmail(String email);
  String generateUserCode();
}
