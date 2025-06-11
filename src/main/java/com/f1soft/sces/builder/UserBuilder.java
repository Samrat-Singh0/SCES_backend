package com.f1soft.sces.builder;

import com.f1soft.sces.dto.UserRequestPayload;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.enums.Status;
import com.f1soft.sces.mapper.UserMapper;
import com.f1soft.sces.util.CommonUtility;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserBuilder {

  public User buildUserForAdd(
      UserRequestPayload userRequestPayload,
      String password
  ) {
    User user = UserMapper.INSTANCE.toUser(userRequestPayload);
    user.setUserCode(CommonUtility.generateUserCode("USR"));
    user.setPassword(password);
    user.setMustChangePassword(true);
    user.setStatus(Status.ACTIVE);
    return user;

//    return User.builder()
//        .userCode(user.getUserCode())
//        .build();
  }

}
