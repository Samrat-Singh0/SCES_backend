package com.f1soft.sces.service;

import com.f1soft.sces.dto.ChangePasswordRequest;
import com.f1soft.sces.dto.SignupRequest;
import com.f1soft.sces.entities.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
  User registerUser(SignupRequest signupRequest);
  User findUserByEmail(String email);
  String generateUserCode();
  ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest);
}
