package com.f1soft.sces.service;

import com.f1soft.sces.dto.ChangePasswordRequest;
import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.dto.UserRequestPayload;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.model.FilterUser;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UserService {

  ResponseEntity<ResponseDto> getActiveUsers(Pageable pageable);

  ResponseEntity<ResponseDto> getActiveUsers();

  ResponseEntity<ResponseDto> registerUser(UserRequestPayload userRequestPayload);

  User findUserByEmail(String email);

  ResponseEntity<ResponseDto> deleteUser(String code, String remarks);

  ResponseEntity<ResponseDto> updateUser(UserRequestPayload userRequestPayload);

  ResponseEntity<ResponseDto> changePassword(ChangePasswordRequest changePasswordRequest);

  ResponseEntity<ResponseDto> getUserBySearchText(
      FilterUser filterCriteria);
}
