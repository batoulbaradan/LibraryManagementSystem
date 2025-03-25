package com.example.Library.classes.mapper;

import com.example.Library.classes.dto.UserDTO;
import com.example.Library.model.basic.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", source = "id")
    UserDTO toDTO(User user);

    @Mapping(target = "id", source = "id")
    User toEntity(UserDTO userDTO);
}