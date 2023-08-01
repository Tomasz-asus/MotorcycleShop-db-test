package com.example.motorcycleshop.model;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String productDescription;
    private Double productPrice;
    private String imageURL;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    public Product() {
    }

    public Product(String name, String description, Double price, String pictureURL, ProductCategory category){
        this.productName = name;
        this.productDescription = description;
        this.productPrice = price;
        this.imageURL = pictureURL;
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public Long getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public ProductCategory getCategory() {
        return category;
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productPrice=" + productPrice +
                '}';
    }
}
