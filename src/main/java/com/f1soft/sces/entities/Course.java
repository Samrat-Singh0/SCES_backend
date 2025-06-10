package com.f1soft.sces.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@AllArgsConstructor
@NoArgsConstructor
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, unique = true)
  private String code;

  @Column(unique = true, nullable = false)
  private String name;

  @Column(nullable = false)
  private int creditHours;

  @Column(nullable = false)
  private int fullMarks;

  @ManyToOne
  @JoinColumn(name = "semester_id")
  private Semester semester;

  @ManyToOne
  @JoinColumn(name = "instructor_id")
  private Instructor instructor;
}
