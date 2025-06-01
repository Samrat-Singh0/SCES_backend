package com.f1soft.sces.auth;

import com.f1soft.sces.dto.AuthResponse;
import com.f1soft.sces.dto.LoginRequest;

public interface AuthService {
  AuthResponse login(LoginRequest loginRequest);
}
