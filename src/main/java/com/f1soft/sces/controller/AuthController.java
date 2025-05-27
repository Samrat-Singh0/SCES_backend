package com.f1soft.sces.controller;

import com.f1soft.sces.models.LoginRequest;
import com.f1soft.sces.models.LoginResponse;
import com.f1soft.sces.models.SignupRequest;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.security.CustomUserDetailService;
import com.f1soft.sces.security.JwtUtil;
import com.f1soft.sces.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  private final AuthenticationManager authenticationManager;

  private final CustomUserDetailService customUserDetailService;

  private final JwtUtil jwtUtil;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
            loginRequest.getPassword())
    );

    final UserDetails userDetails = customUserDetailService.loadUserByUsername(loginRequest.getEmail());
    final String jwt = jwtUtil.generateToken(userDetails);

    return ResponseEntity.ok(new LoginResponse(jwt));
  }


  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
    System.out.println("Controller:"+signupRequest.getPhoneNumber());
    User newUser = userService.registerUser(signupRequest);
    return ResponseEntity.ok(newUser);
  }
}
