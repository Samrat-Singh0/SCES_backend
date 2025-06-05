package com.f1soft.sces.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Semester {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long semesterId;

  @Column(nullable = false)
  private String label;

  @Column(precision = 19, scale = 2, nullable = false)
  private BigDecimal semesterFee;

}
