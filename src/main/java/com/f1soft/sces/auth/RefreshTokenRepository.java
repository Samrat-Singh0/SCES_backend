package com.f1soft.sces.auth;

import com.f1soft.sces.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByRefreshToken(String refreshToken);
  void deleteByRefreshToken(User user);
}
