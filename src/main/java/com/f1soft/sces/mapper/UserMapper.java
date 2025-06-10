package com.f1soft.sces.mapper;

import com.f1soft.sces.dto.UserRequestPayload;
import com.f1soft.sces.entities.User;
import java.util.List;
import org.mapstruct.Mapper;
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
}
