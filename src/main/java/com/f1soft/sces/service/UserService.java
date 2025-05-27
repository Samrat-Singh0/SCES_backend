package com.f1soft.sces.service;

import com.f1soft.sces.models.LoginRequest;
import com.f1soft.sces.models.LoginResponse;
import com.f1soft.sces.models.SignupRequest;
import com.f1soft.sces.entities.User;

public interface UserService {
  LoginResponse login(LoginRequest loginRequest);
  User registerUser(SignupRequest signupRequest);
  User findUserByEmail(String email);
  String generateUserCode();
}
