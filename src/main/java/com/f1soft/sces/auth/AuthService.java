package com.f1soft.sces.auth;

import com.f1soft.sces.dto.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
  ResponseEntity<?> login(LoginRequest loginRequest);
}
