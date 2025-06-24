package com.example.mainBase.auth;

import com.example.mainBase.entities.User;

public interface RefreshTokenService {

  RefreshToken createRefreshToken(User user);

  RefreshToken verifyRefreshToken(RefreshToken refreshToken);
}
