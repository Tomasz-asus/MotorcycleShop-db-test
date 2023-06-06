package com.example.motorcycleshop.repository;


import com.example.motorcycleshop.model.Basket;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

    Optional<Basket> findById(Long id);

    Optional<Basket> findByBasketName(String name);
    void deleteByBasketName(String name);
}
