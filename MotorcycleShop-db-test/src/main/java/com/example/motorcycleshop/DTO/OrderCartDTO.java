package com.example.motorcycleshop.DTO;

import java.util.Objects;

public class OrderCartDTO {

    private final String firstAndLastName;
    private final String street;
    private final String postalCode;
    private final String city;
    private final Integer phoneNumber;
    private String username;
    private final String basketName;


    public OrderCartDTO(String username,
                        String firstAndLastName,
                        String basketName,
                        String street,
                        String postalCode,
                        String city,
                        Integer phoneNumber) {

        this.username = username;
        this.firstAndLastName = firstAndLastName;
        this.basketName = basketName;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstAndLastName() {
        return firstAndLastName;
    }

    public String getBasketName() {
        return basketName;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderCartDTO that)) return false;
        return Objects.equals(username, that.username)
                && Objects.equals(firstAndLastName, that.firstAndLastName)
                && Objects.equals(basketName, that.basketName)
                && Objects.equals(street, that.street)
                && Objects.equals(postalCode, that.postalCode)
                && Objects.equals(city, that.city)
                && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstAndLastName, basketName, street, postalCode, city, phoneNumber);
    }
}
