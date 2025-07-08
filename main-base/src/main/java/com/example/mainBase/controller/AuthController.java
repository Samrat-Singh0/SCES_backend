package com.example.mainBase.controller;

import com.example.mainBase.service.AuthService;
import com.example.mainBase.dto.ChangePasswordRequest;
import com.example.mainBase.dto.LoginRequest;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<ResponseDto> login(@RequestBody LoginRequest loginRequest) {
    return authService.login(loginRequest);
  }

  @PostMapping("/change-password")
  public ResponseEntity<ResponseDto> changePassword(
      @RequestBody ChangePasswordRequest changePasswordRequest) {
    return userService.changePassword(changePasswordRequest);
  }

  @PostMapping("/refresh")
  public ResponseEntity<ResponseDto> refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
    return authService.loginWithRefresh(refreshToken);
  }
}
