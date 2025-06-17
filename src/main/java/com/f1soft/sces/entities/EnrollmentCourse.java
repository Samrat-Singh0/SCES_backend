package com.f1soft.sces.entities;

import com.f1soft.sces.compositeKey.EnrollmentCourseId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentCourse {

  @EmbeddedId
  private EnrollmentCourseId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("enrollmentId")
  @JoinColumn(name = "enrollment_id", nullable = false)
  private Enrollment enrollment;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("courseId")
  @JoinColumn(name = "course_id", nullable = false)
  private Course course;
}
