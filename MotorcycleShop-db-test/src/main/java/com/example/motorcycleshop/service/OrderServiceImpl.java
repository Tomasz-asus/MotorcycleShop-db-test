package com.example.motorcycleshop.service;

import com.example.motorcycleshop.DTO.OrderCartDTO;
import com.example.motorcycleshop.exceptions.BasketNotFoundException;
import com.example.motorcycleshop.exceptions.UserNotFoundException;
import com.example.motorcycleshop.mapper.OrderCartMapper;
import com.example.motorcycleshop.model.AppUser;
import com.example.motorcycleshop.model.Basket;
import com.example.motorcycleshop.model.OrderCart;
import com.example.motorcycleshop.repository.AppUserRepository;
import com.example.motorcycleshop.repository.BasketRepository;
import com.example.motorcycleshop.repository.OrderCartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Transactional
public class OrderServiceImpl implements OrderService{

    private final BasketRepository basketRepository;
    private final OrderCartRepository orderCartRepository;
    private final AppUserRepository appUserRepository;

    public OrderServiceImpl(BasketRepository basketRepository,
                            OrderCartRepository orderCartRepository,
                            AppUserRepository appUserRepository) {
        this.basketRepository = basketRepository;
        this.orderCartRepository = orderCartRepository;
        this.appUserRepository = appUserRepository;
    }


    public OrderCartDTO addOrder(OrderCartDTO orderDTO) {
        OrderCart save = orderCartRepository.save(OrderCartMapper.fromDTO(orderDTO));
        AppUser appUser = appUserRepository.findByUsername(orderDTO.getUsername()).orElseThrow(()
                -> new UserNotFoundException("User " + orderDTO.getUsername() + " was not found."));
        appUser.getOrderCarts().add(save);
        String basketCustomName = UUID.randomUUID().toString().substring(0, 20);
        Basket basket = new Basket(basketCustomName);
        basketRepository.save(basket);
        appUser.setBasket(basketRepository.findByBasketName(basketCustomName).orElseThrow(() ->
                new BasketNotFoundException("Basket " + basketCustomName + " was not found.")));
        appUserRepository.save(appUser);
        return OrderCartMapper.fromEntity(save);
    }
}
