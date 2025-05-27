package com.f1soft.sces.service;

import com.f1soft.sces.entities.Instructor;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleEntityFactoryImpl implements UserRoleEntityFactory {


  @Override
  public Object createRoleEntity(User user) {
    if ("user".equalsIgnoreCase(String.valueOf(user.getRole()))) {
      Student student = new Student();
      student.setUser(user);
      student.setStudentCode("STU-"+System.currentTimeMillis());
      student.setStatus("ACTIVE");
      return student;
    } else {
      Instructor instructor = new Instructor();
      instructor.setUser(user);
      instructor.setInstructorCode("INS-"+System.currentTimeMillis());
      return instructor;
    }
  }
}
