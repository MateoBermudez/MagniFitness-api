package com.devcrew.productmicroservice.controller;

import com.devcrew.productmicroservice.dto.ProductDTO;
import com.devcrew.productmicroservice.dto.ProductRequest;
import com.devcrew.productmicroservice.dto.ProductUserDTO;
import com.devcrew.productmicroservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductRequest productRequest) {
        productService.addProduct(productRequest);
        return ResponseEntity.ok(productRequest.getProductDTO());
    }

    @PutMapping("/update")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductRequest productRequest) {
        productService.updateProduct(productRequest);
        return ResponseEntity.ok(productRequest.getProductDTO());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id, @RequestBody ProductUserDTO productUserDTO) {
        productService.deleteProduct(id, productUserDTO);
        return ResponseEntity.noContent().build();
    }
}