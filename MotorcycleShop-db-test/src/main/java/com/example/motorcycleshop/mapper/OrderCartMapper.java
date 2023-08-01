package com.example.motorcycleshop.mapper;

import com.example.motorcycleshop.DTO.OrderCartDTO;
import com.example.motorcycleshop.exceptions.BasketNotFoundException;
import com.example.motorcycleshop.model.Product;
import com.example.motorcycleshop.model.OrderCart;
import com.example.motorcycleshop.repository.BasketRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderCartMapper {

    private static BasketRepository basketRepository;

    public static OrderCart fromDTO(OrderCartDTO orderCartDTO) {

        return new OrderCart(mappingProductsFromBasket(
                orderCartDTO.getBasketName()),
                orderCartDTO.getStreet(),
                orderCartDTO.getPostalCode(),
                orderCartDTO.getCity(),
                orderCartDTO.getPhoneNumber(),
                LocalDateTime.now(),
                orderCartDTO.getFirstAndLastName(),
                orderCartDTO.getUsername(),
                orderCartDTO.getBasketName()
        );
    }

    public static OrderCartDTO fromEntity(OrderCart orderCart) {

        return new OrderCartDTO(orderCart.getUsername(),
                orderCart.getFirstAndLastName(),
                orderCart.getBasketName(),
                orderCart.getStreet(),
                orderCart.getPostalCode(),
                orderCart.getCity(),
                orderCart.getPhoneNumber());
    }

    private static List<Product> mappingProductsFromBasket(String basketName) {

        return new ArrayList<>(basketRepository.findByBasketName(basketName).orElseThrow(()
                        ->new BasketNotFoundException("Basket " + basketName + "was not found."))

                .getProducts());
    }
}
