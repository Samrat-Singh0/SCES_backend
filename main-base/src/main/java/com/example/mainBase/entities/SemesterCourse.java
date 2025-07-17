package com.example.mainBase.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SemesterCourse {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="semester_id", nullable = false)
  private Semester semester;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", nullable = false)
  private Course course;

  @ManyToMany
      @JoinTable(
          name = "semester_course_instructor",
          joinColumns = @JoinColumn(name = "semester_course_id"),
          inverseJoinColumns = @JoinColumn(name = "instructor_id")
      )
  List<Instructor> instructors;

  @OneToMany(mappedBy = "semesterCourse", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SemesterCourseInstructor> semesterCourseInstructors;
}
