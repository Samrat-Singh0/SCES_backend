package com.f1soft.sces.service;

import com.f1soft.sces.models.SignupRequest;
import com.f1soft.sces.mapper.UserMapper;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }


  @Override
  public User registerUser(SignupRequest signupRequest) {
    if(userRepository.findByEmail(signupRequest.getEmail()).isPresent()){
      throw new IllegalArgumentException("Email already in use");
    }

    User user = UserMapper.INSTANCE.toUser(signupRequest);
    user.setUserCode(generateUserCode());
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    System.out.println(user.getPhoneNumber());
    return userRepository.save(user);
  }

  @Override
  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(()-> new UsernameNotFoundException("User not found with email: " + email));
  }

  @Override
  public String generateUserCode() {
    return "USR-" + System.currentTimeMillis();
  }


}
