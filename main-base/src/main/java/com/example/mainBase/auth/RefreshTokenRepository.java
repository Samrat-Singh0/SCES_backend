package com.example.mainBase.auth;

import com.example.mainBase.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByRefreshToken(String refreshToken);
  void deleteByRefreshToken(User user);
}
