package com.f1soft.sces.service;

import com.f1soft.sces.entities.Instructor;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.enums.EnrollStatus;
import com.f1soft.sces.util.CommonUtility;
import org.springframework.stereotype.Service;

@Service
public class UserRoleEntityFactoryImpl implements UserRoleEntityFactory {

  @Override
  public Object createRoleEntity(User user) {
    if ("student".equalsIgnoreCase(String.valueOf(user.getRole()))) {
      return Student.builder()
          .user(user)
          .code(CommonUtility.generateUserCode("STU"))
          .enrollStatus(EnrollStatus.NEW)
          .build();
    } else {
      Instructor instructor = new Instructor();
      instructor.setUser(user);
      instructor.setCode(CommonUtility.generateUserCode("INS"));
      return instructor;
    }
  }
}
