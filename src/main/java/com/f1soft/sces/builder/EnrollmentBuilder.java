package com.f1soft.sces.builder;

import com.f1soft.sces.entities.Enrollment;
import com.f1soft.sces.entities.Semester;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.enums.CompletionStatus;
import com.f1soft.sces.enums.PaidStatus;
import com.f1soft.sces.util.CommonUtility;
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
    enrollment.setCompletionStatus(CompletionStatus.RUNNING);
    enrollment.setPaidStatus(PaidStatus.UNPAID);
    enrollment.setOutstandingFee(semester.getFee());
//    enrollment.setCourses(enrollment.getCourses());
    return enrollment;
  }
}
