package com.example.motorcycleshop.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Product> products = new ArrayList<>();

    private String basketName;


    public Basket() {
    }

    public Basket(String basketName) {
        this.basketName = basketName;

    }

    public Long getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProductToBasket(Product product) {
        products.add(product);
    }

    public void removeProductFromBasket (Product product) {
        products.remove(product);
    }

    public String getBasketName() {
        return basketName;
    }


}
