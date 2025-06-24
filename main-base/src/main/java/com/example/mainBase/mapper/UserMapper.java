package com.example.mainBase.mapper;

import com.example.mainBase.dto.UserRequestPayload;
import com.example.mainBase.entities.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User toUser(UserRequestPayload userRequestPayload);

  UserRequestPayload toUserDto(User user);

  List<UserRequestPayload> toUserDtoList(List<User> users);

  default Page<UserRequestPayload> toUserDtoPage(Page<User> users) {
    List<UserRequestPayload> userRequest = toUserDtoList(users.getContent());
    return new PageImpl<>(userRequest, users.getPageable(), users.getTotalElements());
  }

  void updateUser(UserRequestPayload userRequestPayload, @MappingTarget User user);
}
