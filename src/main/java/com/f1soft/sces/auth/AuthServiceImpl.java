package com.f1soft.sces.auth;

import com.f1soft.sces.auth.jwt.JwtUtil;
import com.f1soft.sces.dto.AuthResponse;
import com.f1soft.sces.dto.LoginRequest;
import com.f1soft.sces.dto.LoginUserResponse;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.security.CustomUserDetailService;
import com.f1soft.sces.service.AuditLogService;
import com.f1soft.sces.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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

  public AuthResponse login(LoginRequest loginRequest) {
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
        .build();

    auditLogService.log(user, "Logged-In", "","");
    System.out.println(jwt);
    return new AuthResponse(jwt, loginUserResponse);            //wrapper for jwt and loginned user ko details.
  }
}
