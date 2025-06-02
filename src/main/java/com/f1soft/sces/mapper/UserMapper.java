package com.f1soft.sces.mapper;

import com.f1soft.sces.dto.SignupRequest;
import com.f1soft.sces.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  SignupRequest toSignupRequest(User user);

  @Mapping(target = "id",ignore = true)
  User toUser(SignupRequest signupRequest);

}
