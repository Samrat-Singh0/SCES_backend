package com.example.mainBase.controller;

import com.example.mainBase.dto.ResponseDto;
import com.example.mainBase.dto.UserRequestPayload;
import com.example.mainBase.model.FilterUser;
import com.example.mainBase.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @GetMapping("/paged")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> getActiveUsers(
      @PageableDefault(size = 10) Pageable pageable
  ) {
    return userService.getActiveUsers(pageable);
  }

  @GetMapping("/list")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> getActiveUsers() {
    return userService.getActiveUsers();
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> addUser(
      @RequestBody UserRequestPayload userRequestPayload) {
    return userService.registerUser(userRequestPayload);
  }

  @PutMapping("/update")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> updateUser(
      @RequestBody UserRequestPayload userRequestPayload) {
    return userService.updateUser(userRequestPayload);
  }

  @PostMapping("/delete/{code}")
  @PreAuthorize("hasRole(@securityRoles.SUPER)")
  public ResponseEntity<ResponseDto> deleteUser(@PathVariable String code,
      @RequestBody String remarks) {
    return userService.deleteUser(code, remarks);
  }

  @PostMapping("/search")
  public ResponseEntity<ResponseDto> searchUsers(
      @RequestBody FilterUser filterCriteria, @PageableDefault(size = 5, page = 0, sort = "firstName") Pageable pageable) {
    return userService.getUserBySearchText(filterCriteria, pageable);
  }
}
