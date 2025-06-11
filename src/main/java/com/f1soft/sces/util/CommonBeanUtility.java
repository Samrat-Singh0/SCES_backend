package com.f1soft.sces.util;

import com.f1soft.sces.entities.User;
import com.f1soft.sces.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommonBeanUtility {

  private final UserRepository userRepository;

  public User getLoggedUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }
    return userRepository.findByEmail(authentication.getName()).orElseThrow(
        () -> new UsernameNotFoundException(
            "User not found with email: " + authentication.getName()));
  }
}
