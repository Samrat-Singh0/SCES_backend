package com.example.mainBase.service;

import com.example.mainBase.entities.Instructor;
import com.example.mainBase.entities.Student;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.EnrollStatus;
import com.example.mainBase.util.CommonUtility;
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
    } else if("instructor".equalsIgnoreCase(String.valueOf(user.getRole()))) {
      Instructor instructor = new Instructor();
      instructor.setUser(user);
      instructor.setCode(CommonUtility.generateUserCode("INS"));
      return instructor;
    } else {
      return null;
    }
  }
}
