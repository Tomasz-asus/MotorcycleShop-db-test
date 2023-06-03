package com.example.motorcycleshop.repository;

import com.example.motorcycleshop.model.OrderCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderCartRepository extends JpaRepository<OrderCart, Long> {


}
