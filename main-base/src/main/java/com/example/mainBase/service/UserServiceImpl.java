package com.example.mainBase.service;

import com.example.mainBase.builder.UserBuilder;
import com.example.mainBase.dto.ChangePasswordRequest;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.dto.UserRequestPayload;
import com.example.mainBase.entities.Instructor;
import com.example.mainBase.entities.Student;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.ActiveStatus;
import com.example.mainBase.enums.AuditAction;
import com.example.mainBase.mapper.UserMapper;
import com.example.mainBase.model.FilterUser;
import com.example.mainBase.repository.InstructorRepository;
import com.example.mainBase.repository.StudentRepository;
import com.example.mainBase.repository.UserRepository;
import com.example.mainBase.specification.UserSpecification;
import com.example.mainBase.util.CommonBeanUtility;
import com.example.mainBase.util.ResponseBuilder;
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
    List<User> users = userRepository.findAllByActiveStatus(ActiveStatus.ACTIVE);
    if (users.isEmpty()) {
      return ResponseBuilder.getFailedMessage("No active users found");
    }
    List<UserRequestPayload> userRequestPayload = UserMapper.INSTANCE.toUserDtoList(users);
    return ResponseBuilder.success("Fetch users successfully", userRequestPayload);
  }

  @Override
  public ResponseEntity<ResponseDto> getActiveUsers(Pageable pageable) {
    Page<User> userPage = userRepository.findAllByActiveStatus(ActiveStatus.ACTIVE, pageable);
    Page<UserRequestPayload> userRequestPayloadPage = UserMapper.INSTANCE.toUserDtoPage(userPage);

    return ResponseBuilder.success("Fetched Users Successfully", userRequestPayloadPage);
  }

  @Override
  @Transactional
  public ResponseEntity<ResponseDto> registerUser(UserRequestPayload userRequestPayload) {
    Optional<User> user = userRepository.findByEmail(userRequestPayload.getEmail());
    if (user.isPresent()) {
      return ResponseBuilder.getFailedMessage("Email already in use");
    }

    String password = generateRandomPassword();
    System.out.println(password);
    User newUser = UserBuilder.buildUserForAdd(userRequestPayload, passwordEncoder.encode(password));
    userRepository.save(newUser);

    Object userRoleEntity = userRoleEntityFactoryImpl.createRoleEntity(newUser);
    if (userRoleEntity instanceof Student student) {                                      //conditional entry of data in different tables
      studentRepository.save(student);
    } else if (userRoleEntity instanceof Instructor instructor) {
      instructorRepository.save(instructor);
    }

    User loggedInUser = commonBeanUtility.getLoggedInUser();
    auditLogService.log(loggedInUser, AuditAction.SIGNED_UP, "User",
        newUser.getId());

    sendEmail(userRequestPayload.getEmail(), password);
    return ResponseBuilder.success("New User Added.", null);
  }

  @Override
  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
  }

  @Override
  public ResponseEntity<ResponseDto> deleteUser(String code, String remarks) {
    User user = userRepository.findByCode(code);

    if (user == null) {
      return ResponseBuilder.getFailedMessage("User not found");
    }

    user.setActiveStatus(ActiveStatus.INACTIVE);
    user.setRemarks(remarks);
    userRepository.save(user);
    return ResponseBuilder.success("User deleted.", null);
  }

  @Override
  public ResponseEntity<ResponseDto> updateUser(UserRequestPayload userRequestPayload) {
    try {
      User user = userRepository.findByCode(userRequestPayload.getCode());
      UserMapper.INSTANCE.updateUser(userRequestPayload, user);

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
      List<User> userList = userRepository.findAllByActiveStatus(ActiveStatus.ACTIVE);

      if (userList.isEmpty()) {
        return ResponseBuilder.getFailedMessage("No any active users");
      }

//      if (filterCriteria.getFirstName() == null && filterCriteria.getMiddleName() == null
//          && filterCriteria.getLastName() == null && filterCriteria.getRole() == null
//          && filterCriteria.getPhoneNumber() == null) {
//        users = UserMapper.INSTANCE.toUserDtoList(userList);
//        return ResponseBuilder.success("Fetched User Successfully", users);
//      }

      Specification<User> specification = UserSpecification.buildSpec(filterCriteria);

      users = userRepository.findAll(specification).stream()
          .map(UserMapper.INSTANCE::toUserDto)
          .collect(Collectors.toList());

      return ResponseBuilder.success("Fetched User Successfully", users);

    } catch (Exception e) {
      return ResponseBuilder.getFailedMessage(e.getMessage());
    }
  }


  private String generateRandomPassword() {
    return UUID.randomUUID().toString();
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