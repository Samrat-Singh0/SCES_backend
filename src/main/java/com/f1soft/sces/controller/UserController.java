package com.f1soft.sces.controller;

import com.f1soft.sces.dto.UserDto;
import com.f1soft.sces.model.FilterUser;
import com.f1soft.sces.service.UserService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/super/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/get")
  public ResponseEntity<List<UserDto>> getActiveUsers() {
    List<UserDto> users = userService.getActiveUsers();
    return ResponseEntity.ok(users);
  }

  @DeleteMapping("/delete/{code}")
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  public ResponseEntity<?> deleteUser(@PathVariable String code) {
    userService.deleteUser(code);
    return ResponseEntity.ok().body(null);
  }

  @PutMapping("/update")
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  public ResponseEntity<Map<String, String>> updateUser(@RequestBody UserDto userDto) {
    String result = userService.updateUser(userDto);
    return ResponseEntity.ok(Map.of("message", result));
  }

  @GetMapping("/search")
  public ResponseEntity<List<UserDto>> searchUsers(FilterUser filterCriteria) {
    return ResponseEntity.ok(userService.getUserBySearchText(filterCriteria));
  }
}
