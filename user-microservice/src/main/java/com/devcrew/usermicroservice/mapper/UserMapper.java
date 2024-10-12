package com.devcrew.usermicroservice.mapper;

import com.devcrew.usermicroservice.dto.UserDTO;
import com.devcrew.usermicroservice.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(AppUser appUser);

    AppUser toEntity(UserDTO appUserDTO);
}
