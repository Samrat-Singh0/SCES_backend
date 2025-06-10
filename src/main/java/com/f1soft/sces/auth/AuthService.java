package com.f1soft.sces.auth;

import com.f1soft.sces.dto.LoginRequest;
import com.f1soft.sces.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

  ResponseEntity<ResponseDto> login(LoginRequest loginRequest);
}
