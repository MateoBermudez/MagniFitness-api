package com.devcrew.productmicroservice.mapper;

import com.devcrew.productmicroservice.dto.ProductDTO;
import com.devcrew.productmicroservice.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "id", target = "productId")
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "description", target = "productDescription")
    @Mapping(source = "price", target = "productPrice")
    @Mapping(source = "stock", target = "productStock")
    @Mapping(source = "category", target = "productCategory")
    ProductDTO toProductDTO(Product product);

    @Mapping(source = "productId", target = "id")
    @Mapping(source = "productName", target = "name")
    @Mapping(source = "productDescription", target = "description")
    @Mapping(source = "productPrice", target = "price")
    @Mapping(source = "productStock", target = "stock")
    @Mapping(source = "productCategory", target = "category")
    Product toProduct(ProductDTO productDTO);
}