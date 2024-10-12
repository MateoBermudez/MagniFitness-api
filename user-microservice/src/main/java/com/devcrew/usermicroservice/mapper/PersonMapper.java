package com.devcrew.usermicroservice.mapper;

import com.devcrew.usermicroservice.dto.PersonDTO;
import com.devcrew.usermicroservice.model.AppPerson;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper{
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonDTO toDTO(AppPerson appPerson);

    AppPerson toEntity(PersonDTO appPersonDTO);
}
