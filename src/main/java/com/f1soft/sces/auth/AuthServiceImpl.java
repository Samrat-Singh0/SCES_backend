package com.f1soft.sces.auth;

import com.f1soft.sces.auth.jwt.JwtUtil;
import com.f1soft.sces.dto.LoginRequest;
import com.f1soft.sces.dto.LoginUserResponse;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.security.CustomUserDetailService;
import com.f1soft.sces.service.AuditLogService;
import com.f1soft.sces.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final CustomUserDetailService customUserDetailService;
  private final JwtUtil jwtUtil;
  private final UserService userService;
  private final AuditLogService auditLogService;

  public ResponseEntity<?> login(LoginRequest loginRequest) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
              loginRequest.getPassword()));

      final UserDetails userDetails = customUserDetailService.loadUserByUsername(loginRequest.getEmail());
      final String jwt = jwtUtil.generateToken(userDetails);

      User user = userService.findUserByEmail(loginRequest.getEmail());

      LoginUserResponse loginUserResponse = LoginUserResponse.builder()
          .email(user.getEmail())
          .fullName(user.getFullName())
          .role(user.getRole().name())
          .mustChangePassword(user.isMustChangePassword())
          .build();

      auditLogService.log(user, "Logged-In", "","");
      System.out.println(jwt);

      return ResponseEntity.ok()
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
          .body(loginUserResponse);

    }catch(BadCredentialsException e){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }
  }
}
