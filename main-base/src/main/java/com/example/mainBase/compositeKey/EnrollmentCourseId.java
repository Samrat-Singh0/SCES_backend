package com.example.mainBase.compositeKey;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Data;

@Embeddable
@Data
public class EnrollmentCourseId implements Serializable {

  private Long enrollmentId;
  private Long courseId;

}
