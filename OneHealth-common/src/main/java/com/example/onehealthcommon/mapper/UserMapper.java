package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.UserPageDto;
import com.example.onehealthcommon.dto.UserVerifyDto;
import com.example.onehealthcommon.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserVerifyDto map(User user);
    UserPageDto mapToUserPageDto(User user);


}
