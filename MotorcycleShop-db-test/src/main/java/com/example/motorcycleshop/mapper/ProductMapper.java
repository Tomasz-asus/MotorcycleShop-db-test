package com.example.motorcycleshop.mapper;

import com.example.motorcycleshop.model.Product;
import com.example.motorcycleshop.DTO.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public static Product fromDTO(ProductDTO productDTO) {
        return new Product(
                productDTO.getProductName(),
                productDTO.getProductDescription(),
                productDTO.getProductPrice(),
                productDTO.getImageURL(),
                productDTO.getCategory());
    }
    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getProductName(),
                product.getProductDescription(),
                product.getProductPrice(),
                product.getImageURL(),
                product.getCategory());
    }
}
