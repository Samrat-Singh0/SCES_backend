package com.f1soft.sces.service;

import com.f1soft.sces.dto.ChangePasswordRequest;
import com.f1soft.sces.dto.UserDto;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.model.FilterUser;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface UserService {

  List<UserDto> getActiveUsers();
  User registerUser(UserDto userDto);
  User findUserByEmail(String email);
  void deleteUser(String userCode);
  String updateUser(UserDto userDto);
  String generateUserCode();
  ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest);
  List<UserDto> getUserBySearchText(FilterUser filterCriteria);
}
