package com.devcrew.productmicroservice.service;

import com.devcrew.productmicroservice.dto.ProductDTO;
import com.devcrew.productmicroservice.dto.ProductRequest;
import com.devcrew.productmicroservice.dto.ProductUserDTO;
import com.devcrew.productmicroservice.exception.ProductNotFoundException;
import com.devcrew.productmicroservice.mapper.ProductMapper;
import com.devcrew.productmicroservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Cacheable(value = "products")
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper.INSTANCE::toProductDTO)
                .toList();
    }

    @Cacheable(value = "product", key = "#id")
    public ProductDTO getProductById(Integer id) {
        return productRepository.findById(id)
                .map(ProductMapper.INSTANCE::toProductDTO)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));
    }


    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "product", allEntries = true)
        }
    )
    @Transactional
    public void addProduct(ProductRequest productRequest) {
        // There could be products with the same name, and even with the same price, so we don't really need to check if the product already exists
        productRepository.save(ProductMapper.INSTANCE.toProduct(productRequest.getProductDTO()));
    }


    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "product", allEntries = true)
        }
    )
    @Transactional
    public void updateProduct(ProductRequest productRequest) {
        ProductDTO productDTO = productRequest.getProductDTO();
        productRepository.findById(productDTO.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productDTO.getProductId() + " not found."));

        productRepository.save(ProductMapper.INSTANCE.toProduct(productDTO));
    }

    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "product", allEntries = true)
        }
    )
    @Transactional
    public void deleteProduct(Integer id, ProductUserDTO productUserDTO) {
        productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));

        productRepository.deleteById(id);
    }
}