package com.f1soft.sces.service;

import com.f1soft.sces.dto.ChangePasswordRequest;
import com.f1soft.sces.dto.SignupRequest;
import com.f1soft.sces.entities.Instructor;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.mapper.UserMapper;
import com.f1soft.sces.repository.InstructorRepository;
import com.f1soft.sces.repository.StudentRepository;
import com.f1soft.sces.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRoleEntityFactoryImpl userRoleEntityFactoryImpl;
  private final StudentRepository studentRepository;
  private final InstructorRepository instructorRepository;
  private final AuditLogService auditLogService;
  private final EmailService emailService;

  @Override
  @Transactional
  public User registerUser(SignupRequest signupRequest) {
    if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already in use");
    }

    User user = UserMapper.INSTANCE.toUser(signupRequest);

    user.setUserCode(generateUserCode());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setMustChangePassword(true);

    User savedUser = userRepository.save(user);

    Object userRoleEntity = userRoleEntityFactoryImpl.createRoleEntity(savedUser);

    if (userRoleEntity instanceof Student student) {                                      //conditional entry of data in different tables
      studentRepository.save(student);
    } else if (userRoleEntity instanceof Instructor instructor) {
      instructorRepository.save(instructor);
    }

    auditLogService.log(user, "Signed Up", "", "");             //log the event.

    sendEmail(signupRequest.getEmail(), signupRequest.getPassword());

    return userRepository.save(user);
  }

  @Override
  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
  }

  @Override
  public String generateUserCode() {
    return "USR-" + System.currentTimeMillis();
  }

  public void sendEmail(String email, String password) {

    String subject = "Your SCES Account Credentials";
    String body = "<p>Welcome to SCES!</p>" +
        "<p>Here are your login credentials. Please use these for the initial login process. " +
        "You will be asked to change your password on first login. After that, use your new password to sign in.</p>"
        +
        "<ul>" +
        "<li><strong>Email:</strong> " + email + "</li>" +
        "<li><strong>Password:</strong> " + password + "</li>" +
        "</ul>" +
        "<p><a href=\"http://localhost:4200/login\">Click here to sign in</a></p>";

    emailService.sendEmail(email, subject, body);
  }

  @Override
  public ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest) {
    User user = userRepository.findByEmail(changePasswordRequest.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + changePasswordRequest.getEmail()));

    user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
    user.setMustChangePassword(false);
    userRepository.save(user);

    return ResponseEntity.ok().build();
  }
}