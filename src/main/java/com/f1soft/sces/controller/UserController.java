package com.f1soft.sces.controller;

import com.f1soft.sces.dto.ResponseDto;
import com.f1soft.sces.dto.UserRequestPayload;
import com.f1soft.sces.model.FilterUser;
import com.f1soft.sces.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/super/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/pageList")
  public ResponseEntity<ResponseDto> getActiveUsers(
      Pageable pageable
  ) {
    return userService.getActiveUsers(pageable);
  }

  @GetMapping("/list")
  public ResponseEntity<ResponseDto> getActiveUsers() {
    return userService.getActiveUsers();
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole('SUPER_ADMIN')") // todo: use constant
  public ResponseEntity<ResponseDto> addUser(
      @RequestBody UserRequestPayload userRequestPayload) {
    return userService.registerUser(userRequestPayload);
  }

  @PutMapping("/update")
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  public ResponseEntity<ResponseDto> updateUser(
      @RequestBody UserRequestPayload userRequestPayload) {
    return userService.updateUser(userRequestPayload);
  }

  @DeleteMapping("/delete/{code}")
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  public ResponseEntity<ResponseDto> deleteUser(@PathVariable String code) {
    return userService.deleteUser(code);
  }

  @PostMapping("/search")
  public ResponseEntity<ResponseDto> searchUsers(
      @RequestBody FilterUser filterCriteria) {
    return userService.getUserBySearchText(filterCriteria);
  }
}
