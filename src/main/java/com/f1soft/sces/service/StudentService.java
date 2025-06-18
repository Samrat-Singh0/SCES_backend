package com.f1soft.sces.service;

import com.f1soft.sces.dto.EnrollmentResponsePayload;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.enums.EnrollStatus;

public interface StudentService {

  Student setEnrollStatus(EnrollStatus enrollStatus, EnrollmentResponsePayload payload);
}
