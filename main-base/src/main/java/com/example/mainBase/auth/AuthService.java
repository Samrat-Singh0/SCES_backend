package com.example.mainBase.auth;

import com.example.mainBase.dto.LoginRequest;
import com.example.mainBase.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

  ResponseEntity<ResponseDto> login(LoginRequest loginRequest);
}
