package com.example.mainBase.service;

import com.example.mainBase.dto.ChangePasswordRequest;
import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.dto.UserRequestPayload;
import com.example.mainBase.entities.User;
import com.example.mainBase.model.FilterUser;
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
