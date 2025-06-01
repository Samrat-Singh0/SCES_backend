package com.f1soft.sces.entities;

import com.f1soft.sces.auth.RefreshToken;
import com.f1soft.sces.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String userCode;

  @Column(nullable = false)
  private String fullName;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String address;

  @Column(name = "phone_number", unique = true, nullable = false)
//  @Pattern(regexp = "^98\\d{8}$", message = "Phone number must start with 98 and must be 10 digits.")
  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name="must_change_password",nullable = false)
  private boolean mustChangePassword;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private Student student;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private Instructor instructor;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private RefreshToken refreshToken;
}
