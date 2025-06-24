package com.example.mainBase.builder;

import com.example.mainBase.dto.UserRequestPayload;
import com.example.mainBase.entities.User;
import com.example.mainBase.enums.ActiveStatus;
import com.example.mainBase.mapper.UserMapper;
import com.example.mainBase.util.CommonUtility;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserBuilder {

  public User buildUserForAdd(
      UserRequestPayload userRequestPayload,
      String password
  ) {
    User user = UserMapper.INSTANCE.toUser(userRequestPayload);

    user.setCode(CommonUtility.generateUserCode("USR"));
    user.setPassword(password);
    user.setMustChangePassword(true);
    user.setActiveStatus(ActiveStatus.ACTIVE);
    return user;
  }

}
