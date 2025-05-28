package com.f1soft.sces.service;

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

  @Override
  @Transactional
  public User registerUser(SignupRequest signupRequest) {
    if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already in use");
    }

    User user = UserMapper.INSTANCE.toUser(signupRequest);

    user.setUserCode(generateUserCode());
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    User savedUser = userRepository.save(user);

    Object userRoleEntity = userRoleEntityFactoryImpl.createRoleEntity(savedUser);

    if (userRoleEntity instanceof Student student) {
      studentRepository.save(student);
    } else if (userRoleEntity instanceof Instructor instructor) {
      instructorRepository.save(instructor);
    }

//    User user = findUserByEmail(signupRequest.getEmail());

    auditLogService.log(user, "Signed Up", "", "");
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


}