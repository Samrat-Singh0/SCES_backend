package com.f1soft.sces.auth;

import com.f1soft.sces.dto.LoginRequest;
import com.f1soft.sces.dto.LoginResponse;

public interface AuthService {
  LoginResponse login(LoginRequest loginRequest);
}
