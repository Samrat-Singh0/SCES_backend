package com.f1soft.sces.service;

import com.f1soft.sces.UserSpecification;
import com.f1soft.sces.builder.UserBuilder;
import com.f1soft.sces.dto.ChangePasswordRequest;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.dto.UserRequestPayload;
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
import com.f1soft.sces.util.CommonBeanUtility;
import com.f1soft.sces.util.ResponseBuilder;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
  private final CommonBeanUtility commonBeanUtility;

  @Override
  public ResponseEntity<ResponseDto> getActiveUsers() {
    List<User> users = userRepository.findAllByStatus(Status.ACTIVE);
    if (users.isEmpty()) {
      return ResponseBuilder.getFailedMessage("No active users found");
    }
    List<UserRequestPayload> userRequestPayload = UserMapper.INSTANCE.toUserDtoList(users);
    return ResponseBuilder.success("Fetch users successfully", userRequestPayload);
  }

  @Override
  public ResponseEntity<ResponseDto> getActiveUsers(Pageable pageable) {
    Page<User> users = userRepository.findAllByStatus(Status.ACTIVE, pageable);
    Page<UserRequestPayload> userRequestPayloads = UserMapper.INSTANCE.toUserDtoPage(users);
    return ResponseBuilder.success("Fetched Users Successfully", userRequestPayloads);
  }

  @Override
  @Transactional
  public ResponseEntity<ResponseDto> registerUser(UserRequestPayload userRequestPayload) {
    Optional<User> user = userRepository.findByEmail(userRequestPayload.getEmail());
    if (user.isPresent()) {
      return ResponseBuilder.getFailedMessage("Email already in use");
    }

    String password = generateRandomPassword();
    User newUser = UserBuilder.buildUserForAdd(userRequestPayload, password);
    userRepository.save(newUser);

    Object userRoleEntity = userRoleEntityFactoryImpl.createRoleEntity(newUser);
    if (userRoleEntity instanceof Student student) {                                      //conditional entry of data in different tables
      studentRepository.save(student);
    } else if (userRoleEntity instanceof Instructor instructor) {
      instructorRepository.save(instructor);
    }

    User loggedInUser = commonBeanUtility.getLoggedUserId();
    auditLogService.log(loggedInUser, "Signed Up", "User",
        newUser.getId());             //log the event.

    sendEmail(userRequestPayload.getEmail(), password);
    userRepository.save(newUser);
    return ResponseBuilder.success("New User Added.", null);
  }

  @Override
  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
  }

  @Override
  public ResponseEntity<ResponseDto> deleteUser(String userCode) {
    Optional<User> optionalUser = userRepository.findByUserCode(userCode);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      user.setStatus(Status.INACTIVE);
      userRepository.save(user);
      return ResponseBuilder.success("User deleted.", null);
    } else {
      return ResponseBuilder.getFailedMessage("User not found.");
    }
  }

  @Override
  public ResponseEntity<ResponseDto> updateUser(UserRequestPayload userRequestPayload) {
    try {
      Optional<User> optionalUser = userRepository.findByUserCode(userRequestPayload.getUserCode());
      User user;
      if (optionalUser.isEmpty()) {
        return ResponseBuilder.getFailedMessage("User not found.");
      }
      user = optionalUser.get();
      user.setEmail(userRequestPayload.getEmail());
      user.setAddress(userRequestPayload.getAddress());
      user.setPhoneNumber(userRequestPayload.getPhoneNumber());
      user.setRole(Role.valueOf(userRequestPayload.getRole()));

      userRepository.save(user);
      return ResponseBuilder.success("User Updated.", null);
    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }

  }

  @Override
  public ResponseEntity<ResponseDto> getUserBySearchText(
      FilterUser filterCriteria) {
    try {
      List<UserRequestPayload> users;
      List<User> userList = userRepository.findAllByStatus(Status.ACTIVE);

      if (userList.isEmpty()) {
        return ResponseBuilder.getFailedMessage("No any active users");
      }

      if (filterCriteria.getFullName() == null && filterCriteria.getRole() == null
          && filterCriteria.getPhoneNumber() == null) {
        users = UserMapper.INSTANCE.toUserDtoList(userList);
        return ResponseBuilder.success("Fetched User Successfully", users);
      }

      Specification<User> specification = UserSpecification.buildSpec(filterCriteria);

      users = userRepository.findAll(specification).stream()
          .map(UserMapper.INSTANCE::toUserDto)
          .collect(Collectors.toList());

      return ResponseBuilder.success("Fetched User Successfully", users);

    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
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
  public ResponseEntity<ResponseDto> changePassword(
      ChangePasswordRequest changePasswordRequest) {
    Optional<User> optionalUser = userRepository.findByEmail(changePasswordRequest.getEmail());
    if (optionalUser.isEmpty()) {
      return ResponseBuilder.getFailedMessage("User not found.");
    }

    User user = optionalUser.get();
    user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
    user.setMustChangePassword(false);
    userRepository.save(user);

    return ResponseBuilder.success("Updated User Details Successfully.", null);
  }


}