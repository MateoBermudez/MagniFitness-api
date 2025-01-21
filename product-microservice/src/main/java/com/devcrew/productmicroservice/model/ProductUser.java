package com.devcrew.productmicroservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

// Check if it's really necessary to have a table for the ProductUser entity.
// Check if the ProductUser entity should have a relation to the Product entity.
// I don't think it's necessary because you don't need to know which user added a product to the database.
// Or do you?
// I don't even think you need to have a ProductUser entity.
// For now, let's keep it, and not create a ProductUser service, repository, and controller yet.
@Entity
@Table(
        name = "PRODUCT_USER",
        schema = "dbo"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    private String username;

    private String email;
}
