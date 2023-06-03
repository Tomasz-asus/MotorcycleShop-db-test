package com.example.motorcycleshop.DTO;

import java.util.Objects;

public class OrderCartDTO {

    private String firstAndLastName;
    private String street;
    private String postalCode;
    private String city;
    private Integer phoneNumber;
    private String username;
    private String basketName;


    public OrderCartDTO(String username, String firstAndLastName,
                        String basketName, String street, String postalCode,
                        String city, Integer phoneNumber) {

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

    public void setFirstAndLastName(String firstAndLastName) {
        this.firstAndLastName = firstAndLastName;
    }

    public String getBasketName() {
        return basketName;
    }

    public void setBasketName(String basketName) {
        this.basketName = basketName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderCartDTO)) return false;
        OrderCartDTO that = (OrderCartDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(firstAndLastName, that.firstAndLastName) && Objects.equals(basketName, that.basketName) && Objects.equals(street, that.street) && Objects.equals(postalCode, that.postalCode) && Objects.equals(city, that.city) && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstAndLastName, basketName, street, postalCode, city, phoneNumber);
    }
}
