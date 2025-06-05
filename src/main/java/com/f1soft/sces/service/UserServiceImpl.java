package com.f1soft.sces.service;

import com.f1soft.sces.dto.ChangePasswordRequest;
import com.f1soft.sces.dto.UserDto;
import com.f1soft.sces.entities.Instructor;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.enums.Role;
import com.f1soft.sces.enums.Status;
import com.f1soft.sces.mapper.UserMapper;
import com.f1soft.sces.model.FilterUser;
import com.f1soft.sces.repository.InstructorRepository;
import com.f1soft.sces.repository.StudentRepository;
import com.f1soft.sces.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
  public List<UserDto> getActiveUsers() {
    List<User> users = userRepository.findAllByStatus(Status.ACTIVE);
    return UserMapper.INSTANCE.toUserDtoList(users);
  }

  @Override
  @Transactional
  public User registerUser(UserDto userDto) {
    if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already in use");
    }

    User user = UserMapper.INSTANCE.toUser(userDto);
    String password = generateRandomPassword();

    user.setUserCode(generateUserCode());
    user.setPassword(password);
    user.setMustChangePassword(true);
    user.setStatus(Status.ACTIVE);

    User savedUser = userRepository.save(user);

    Object userRoleEntity = userRoleEntityFactoryImpl.createRoleEntity(savedUser);
    if (userRoleEntity instanceof Student student) {                                      //conditional entry of data in different tables
      studentRepository.save(student);
    } else if (userRoleEntity instanceof Instructor instructor) {
      instructorRepository.save(instructor);
    }

    User loggedInUser = getLoggedUserId();
    auditLogService.log(loggedInUser, "Signed Up", "User", user.getId());             //log the event.

    sendEmail(userDto.getEmail(), password);

    return userRepository.save(user);
  }

  @Override
  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
  }

  @Override
  public void deleteUser(String userCode) {
    User user = userRepository.findByUserCode(userCode).orElseThrow(() -> new UsernameNotFoundException("User not found with code: " + userCode));
    user.setStatus(Status.INACTIVE);
    userRepository.save(user);
  }

  @Override
  public String updateUser(UserDto userDto) {
    try{
      User user = userRepository.findByUserCode(userDto.getUserCode()).orElseThrow(() -> new UsernameNotFoundException("User not found with code: " + userDto.getUserCode()));
      user.setEmail(userDto.getEmail());
      user.setAddress(userDto.getAddress());
      user.setPhoneNumber(userDto.getPhoneNumber());
      user.setRole(Role.valueOf(userDto.getRole()));

      userRepository.save(user);
      return "Successfully updated";
    }catch (Exception e){
      return ("Update Failed: " + e.getMessage());
    }

  }

  @Override
  public List<UserDto> getUserBySearchText(FilterUser filterCriteria) {
    return userRepository.findUserBySearchText(filterCriteria).stream().map(UserMapper.INSTANCE::toUserDto).collect(
        Collectors.toList());
  }

  @Override
  public String generateUserCode() {
    return "USR-" + System.currentTimeMillis();
  }
  public String generateRandomPassword() {
    return passwordEncoder.encode(UUID.randomUUID().toString());
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

  public User getLoggedUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if(authentication == null || !authentication.isAuthenticated()) {
      return null;
    }
    return userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + authentication.getName()));
  }
}