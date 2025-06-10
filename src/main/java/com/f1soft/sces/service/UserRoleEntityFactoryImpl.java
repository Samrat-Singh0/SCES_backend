package com.f1soft.sces.service;

import com.f1soft.sces.entities.Instructor;
import com.f1soft.sces.entities.Student;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.util.CommonUtility;
import org.springframework.stereotype.Service;

@Service
public class UserRoleEntityFactoryImpl implements UserRoleEntityFactory {

  @Override
  public Object createRoleEntity(User user) {
    if ("user".equalsIgnoreCase(String.valueOf(user.getRole()))) {
      return Student.builder()
          .user(user)
          .studentCode(CommonUtility.generateUserCode("STU"))
          .status("ACTIVE")
          .build();
    } else {
      Instructor instructor = new Instructor();
      instructor.setUser(user);
      instructor.setInstructorCode(CommonUtility.generateUserCode("INS"));
      return instructor;
    }
  }
}
