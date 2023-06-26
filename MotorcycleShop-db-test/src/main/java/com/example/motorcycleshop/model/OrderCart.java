package com.example.motorcycleshop.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OrderCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<Product> products = new ArrayList<>();

    private String firstAndLastName;
    private String street;
    private String postalCode;
    private String city;
    private Integer phoneNumber;
    private LocalDateTime orderDate;
    private String username;
    private String basketName;

    public OrderCart() {
    }

    public OrderCart(List<Product> products, String street, String postalCode,
                     String city, Integer phoneNumber, LocalDateTime orderDate,
                     String firstAndLastName, String username, String basketName) {
        this.products = products;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.orderDate = orderDate;
        this.firstAndLastName = firstAndLastName;
        this.username = username;
        this.basketName = basketName;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }


    public String getFirstAndLastName() {
        return firstAndLastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBasketName() {
        return basketName;
    }

}
