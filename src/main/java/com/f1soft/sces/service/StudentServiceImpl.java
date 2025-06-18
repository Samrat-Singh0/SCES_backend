package com.f1soft.sces.service;

import com.f1soft.sces.dto.EnrollmentResponsePayload;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.enums.CompletionStatus;
import com.f1soft.sces.enums.EnrollStatus;
import com.f1soft.sces.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

  private StudentRepository studentRepository;

  @Override
  public Student setEnrollStatus(EnrollStatus enrollStatus, EnrollmentResponsePayload payload) {
    if (payload.getCompletionStatus().equals(
        CompletionStatus.RUNNING)) {                                                        //yedi update pending lai accept gareko ho vaye update student ko enrollStatus as well.
      Student student = studentRepository.findByCode(payload.getStudent().getCode());
      student.setEnrollStatus(EnrollStatus.ENROLLED);
      studentRepository.save(student);
      return student;
    }

    if (payload.getCompletionStatus().equals(CompletionStatus.REJECTED)) {
      Student student = studentRepository.findByCode(payload.getStudent().getCode());
      student.setEnrollStatus(EnrollStatus.REJECTED);
      studentRepository.save(student);
      return student;
    }
    return null;
  }


}
