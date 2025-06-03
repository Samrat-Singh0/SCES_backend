package com.f1soft.sces.controller;

import com.f1soft.sces.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/super-admin/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

//  @GetMapping("/get-all")
//  @PreAuthorize("hasRole('SUPER_ADMIN')")
//  public ResponseEntity<List<UserDto>> getAllUser() {
//    return ResponseEntity.ok().body(userService.getAllUsers());
//  }
}
