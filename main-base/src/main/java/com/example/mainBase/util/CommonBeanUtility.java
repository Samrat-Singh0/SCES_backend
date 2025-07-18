package com.example.mainBase.util;

import com.example.mainBase.entities.User;
import com.example.mainBase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommonBeanUtility {

  private final UserRepository userRepository;

  public User getLoggedInUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }
    return userRepository.findByEmail(authentication.getName()).orElseThrow(
        () -> new UsernameNotFoundException(
            "User not found with email: " + authentication.getName()));
  }

//  public Student setStatusEnrolled(Student student) {
//    student.setEnrollStatus(EnrollStatus.ENROLLED);
//    return student;
//  }
}
