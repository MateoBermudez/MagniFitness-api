package com.devcrew.productmicroservice.mapper;

import com.devcrew.productmicroservice.dto.ProductUserDTO;
import com.devcrew.productmicroservice.model.ProductUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductUserMapper {
    ProductUserMapper INSTANCE = Mappers.getMapper(ProductUserMapper.class);

    ProductUser toProductUser(ProductUserDTO productUserDTO);

    ProductUserDTO toProductUserDTO(ProductUser productUser);

}
