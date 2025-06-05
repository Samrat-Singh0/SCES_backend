package com.f1soft.sces.mapper;

import com.f1soft.sces.dto.UserDto;
import com.f1soft.sces.entities.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User toUser(UserDto userDto);
  UserDto toUserDto(User user);
  List<UserDto> toUserDtoList(List<User> users);

}
