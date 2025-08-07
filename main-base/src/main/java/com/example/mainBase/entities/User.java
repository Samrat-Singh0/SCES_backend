package com.example.mainBase.entities;

import com.example.mainBase.enums.ActiveStatus;
import com.example.mainBase.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  private String code;

  @Column(nullable = false)
  private String firstName;

  private String middleName;

  @Column(nullable = false)
  private String lastName;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String address;

  @Column(name = "phone_number", unique = true, nullable = false)
  private String phoneNumber;

  @Column(name = "must_change_password", nullable = false)
  private boolean mustChangePassword;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ActiveStatus activeStatus;

  private String remarks;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private com.example.mainBase.entities.Role newRole;
}
