package com.example.mainBase.entities;

import com.example.attendance_fee_lib.enums.FeeStatus;
import com.example.mainBase.enums.CompletionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id", nullable = false)
  private Student student;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "semester_id", nullable = false)
  private Semester semester;

  private LocalDate enrolledDate;

  private LocalDate completionDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CompletionStatus completionStatus;

  @Column(nullable = false)
  private BigDecimal outstandingFee;

  @Column(nullable = false, unique = true)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FeeStatus paidStatus;

  @ManyToMany
  @JoinTable(
      name = "enrollment_course",
      joinColumns = @JoinColumn(name = "enrollment_id"),
      inverseJoinColumns = @JoinColumn(name = "course_id")
  )
  private List<Course> courses;
}
