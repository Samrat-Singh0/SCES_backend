package com.f1soft.sces.mapper;

import com.f1soft.sces.dto.SignupRequest;
import com.f1soft.sces.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  SignupRequest toSignupRequest(User user);

  User toUser(SignupRequest signupRequest);
//
//  UserDto toUserDto(User user);
//  List<UserDto> toUserDtoList(List<User> users);

}
