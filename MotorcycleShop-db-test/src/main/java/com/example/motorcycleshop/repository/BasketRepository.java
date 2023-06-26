package com.example.motorcycleshop.repository;


import com.example.motorcycleshop.model.Basket;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

    @NotNull Optional<Basket> findById(@NotNull Long id);

    Optional<Basket> findByBasketName(String name);
    void deleteByBasketName(String name);
}
