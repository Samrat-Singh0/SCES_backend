package com.f1soft.sces.auth;

import com.f1soft.sces.entities.User;

public interface RefreshTokenService {

  RefreshToken createRefreshToken(User user);

  RefreshToken verifyRefreshToken(RefreshToken refreshToken);
}
