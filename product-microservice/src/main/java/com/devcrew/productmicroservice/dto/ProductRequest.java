package com.devcrew.productmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private ProductDTO productDTO;

    private ProductUserDTO productUserDTO;
}
