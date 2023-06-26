package com.example.motorcycleshop.service;

import com.example.motorcycleshop.DTO.ProductDTO;
import com.example.motorcycleshop.DTO.OrderCartDTO;
import com.example.motorcycleshop.model.Basket;
import com.example.motorcycleshop.model.Product;

import java.util.List;

public interface MotorcycleShopService {

    ProductDTO addProduct(ProductDTO productAlt);

    List<ProductDTO> getAllProducts();

    List<Basket> getAllBaskets();

    void deleteProduct(String name);

    void deleteBasket(String name);

    void deleteProductFromBasket(String basket, String productName);

    void addProductToBasket(String basketName, String productName);

    List<Product> getALlProductsFromBasket(String basketName);

    OrderCartDTO addOrder(OrderCartDTO orderDTO);
}
