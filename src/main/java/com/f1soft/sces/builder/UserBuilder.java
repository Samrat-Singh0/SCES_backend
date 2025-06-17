package com.f1soft.sces.builder;

import com.f1soft.sces.dto.UserRequestPayload;
import com.f1soft.sces.entities.User;
import com.f1soft.sces.enums.ActiveStatus;
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

    user.setCode(CommonUtility.generateUserCode("USR"));
    user.setPassword(password);
    user.setMustChangePassword(true);
    user.setActiveStatus(ActiveStatus.ACTIVE);
    return user;
  }

}
