package com.example.motorcycleshop.DTO;

import com.example.motorcycleshop.model.ProductCategory;

import java.util.Objects;

public class ProductDTO {

    private final String productName;
    private final String productDescription;
    private final Double productPrice;
    private final String imageURL;
    private final ProductCategory category;

    public ProductDTO(String productName,
                      String productDescription,
                      Double productPrice,
                      String imageURL,
                      ProductCategory category) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.imageURL = imageURL;
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }


    public String getProductDescription() {
        return productDescription;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public String getImageURL() {
        return imageURL;
    }


    public ProductCategory getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return Objects.equals(productName, that.productName)
                && Objects.equals(productDescription, that.productDescription)
                && Objects.equals(productPrice, that.productPrice)
                && Objects.equals(imageURL, that.imageURL)
                && category == that.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, productDescription, productPrice, imageURL, category);
    }
}
