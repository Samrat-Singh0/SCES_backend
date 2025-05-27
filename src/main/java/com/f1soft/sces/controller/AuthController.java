package com.f1soft.sces.controller;

import com.f1soft.sces.entities.User;
import com.f1soft.sces.models.LoginRequest;
import com.f1soft.sces.models.LoginResponse;
import com.f1soft.sces.models.SignupRequest;
import com.f1soft.sces.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    LoginResponse loginResponse = userService.login(loginRequest);
    return ResponseEntity.ok()
        .header(HttpHeaders.AUTHORIZATION,"Bearer " + loginResponse.getToken())
        .body("Login Success");

  }


  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
    User newUser = userService.registerUser(signupRequest);
    return ResponseEntity.ok(newUser);
  }
}
