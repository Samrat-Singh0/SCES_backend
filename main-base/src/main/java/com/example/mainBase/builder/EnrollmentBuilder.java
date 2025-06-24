package com.example.mainBase.builder;

import com.example.attendance_fee_lib.enums.FeeStatus;
import com.example.mainBase.entities.Enrollment;
import com.example.mainBase.entities.Semester;
import com.example.mainBase.entities.Student;
import com.example.mainBase.enums.CompletionStatus;
import com.example.mainBase.util.CommonUtility;
import java.time.LocalDate;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnrollmentBuilder {

  public Enrollment build(Enrollment enrollment, Semester semester, Student student) {
    enrollment.setCode(CommonUtility.generateCode("EN-"));
    enrollment.setSemester(semester);
    enrollment.setStudent(student);
    enrollment.setEnrolledDate(LocalDate.now());
    enrollment.setCompletionDate(null);
    enrollment.setCompletionStatus(CompletionStatus.PENDING);
    enrollment.setPaidStatus(FeeStatus.UNPAID);
    enrollment.setOutstandingFee(semester.getFee());
//    enrollment.setCourses(enrollment.getCourses());
    return enrollment;
  }
}
