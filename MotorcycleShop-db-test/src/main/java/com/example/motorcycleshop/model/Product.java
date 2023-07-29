package com.example.motorcycleshop.model;

import jakarta.persistence.*;
import java.util.List;

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

    // Warto się zastanowić czy produkt powinien wiedzieć w czyim koszuku jest? Moim zdaniem produkt to produkt,
    // raczej nie będziesz sprawdzał po obiekcie produktu do czyjego koszyka należy. Usunąłbym tą zależność
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Basket> baskets;


    public Product() {
    }

    public Product(String name, String description, Double price, String pictureURL, ProductCategory category){
        this.productName = name;
        this.productDescription = description;
        this.productPrice = price;
        this.imageURL = pictureURL;
        this.category = category;
    }


    public List<Basket> getBaskets() {
        return baskets;
    }

    public void addBasket(Basket basket) {
        this.baskets.add(basket);
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
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

    public void setImageURL(String pictureURL) {
        this.imageURL = pictureURL;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public void setBaskets(List<Basket> baskets) {
        this.baskets = baskets;
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
