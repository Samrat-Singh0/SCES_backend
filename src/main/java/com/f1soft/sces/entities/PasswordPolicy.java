package com.f1soft.sces.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordPolicy {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long passwordPolicyId;

  @Column(nullable = false,unique = true, name="policy_code")
  private String policyCode;

  @Column(nullable = false)
  private String parameters;

  @Column(nullable = false)
  private String regex;

  @Column(nullable = false)
  private Boolean active;
}
