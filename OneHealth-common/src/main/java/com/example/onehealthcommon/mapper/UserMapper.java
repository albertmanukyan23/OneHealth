package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.UserDto;
import com.example.onehealthcommon.dto.UserPageDto;
import com.example.onehealthcommon.dto.UserVerifyDto;
import com.example.onehealthcommon.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Value("http://localhost:8080")
    public String siteUrl;

    public abstract UserVerifyDto map(User user);

    public abstract UserPageDto mapToUserPageDto(User user);

    @Mapping(target = "picName", expression = "java(user.getPicName() != null ? siteUrl + \"/users/getImage?picName=\" + user.getPicName() : null)")
    public abstract UserDto mapToDto(User user);
}
