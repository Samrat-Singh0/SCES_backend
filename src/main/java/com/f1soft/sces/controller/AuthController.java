package com.f1soft.sces.controller;

import com.f1soft.sces.auth.AuthService;
import com.f1soft.sces.dto.ChangePasswordRequest;
import com.f1soft.sces.dto.LoginRequest;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  public ResponseEntity<ResponseDto> changePassword(
      @RequestBody ChangePasswordRequest changePasswordRequest) {
    return userService.changePassword(changePasswordRequest);
  }

}
