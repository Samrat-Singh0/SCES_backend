package com.example.mainBase.auth;

import com.example.mainBase.auth.jwt.JwtUtil;
import com.example.mainBase.dto.LoginRequest;
import com.example.mainBase.dto.LoginUserResponse;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.ActiveStatus;
import com.example.mainBase.enums.AuditAction;
import com.example.mainBase.security.CustomUserDetailService;
import com.example.mainBase.service.AuditLogService;
import com.example.mainBase.service.UserService;
import com.example.mainBase.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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

  public ResponseEntity<ResponseDto> login(LoginRequest loginRequest) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
              loginRequest.getPassword()));

      final UserDetails userDetails = customUserDetailService.loadUserByUsername(
          loginRequest.getEmail());
      final String jwt = jwtUtil.generateToken(userDetails);

      User user = userService.findUserByEmail(loginRequest.getEmail());

      if (user.getActiveStatus().equals(ActiveStatus.INACTIVE)) {
        return ResponseBuilder.getFailedMessage("Your account is inactive");
      }

      LoginUserResponse loginUserResponse = LoginUserResponse.builder()
          .email(user.getEmail())
          .firstName(user.getFirstName())
          .middleName(user.getMiddleName())
          .lastName(user.getLastName())
          .role(user.getRole().name())
          .mustChangePassword(user.isMustChangePassword())
          .build();

      auditLogService.log(user, AuditAction.LOGGED_IN, "", null);

      ResponseDto responseBody = new ResponseDto(true, "Logged-In",
          loginUserResponse);

      return ResponseEntity.ok()
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
          .body(responseBody);

    } catch (BadCredentialsException e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
  }
}
