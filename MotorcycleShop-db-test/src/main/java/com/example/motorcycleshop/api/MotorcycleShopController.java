package com.example.motorcycleshop.api;


import com.example.motorcycleshop.DTO.ProductDTO;
import com.example.motorcycleshop.DTO.OrderCartDTO;
import com.example.motorcycleshop.model.Basket;
import com.example.motorcycleshop.model.Product;
import com.example.motorcycleshop.service.MotorcycleShopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class MotorcycleShopController
{

    private final MotorcycleShopService motorcycleShopService;

    public MotorcycleShopController(MotorcycleShopService motorcycleShopService) {
        this.motorcycleShopService = motorcycleShopService;
    }

    @PostMapping("/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
        return new  ResponseEntity<>(motorcycleShopService.addProduct(productDTO), HttpStatus.CREATED);
    }

    @GetMapping("/products")
    @ResponseBody
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok().body(motorcycleShopService.getAllProducts());
    }

    @GetMapping("/baskets")
    @ResponseBody
    public ResponseEntity<List<Basket>> getAllBaskets() {
        return ResponseEntity.ok().body(motorcycleShopService.getAllBaskets());
    }

    @DeleteMapping("/product/{name}")
    @ResponseBody
    public ResponseEntity<Void> removeProduct(@PathVariable String name) {
        motorcycleShopService.deleteProduct(name);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/product/fromBasket/{basketName}/{productName}")
    @ResponseBody
    public ResponseEntity<Void> removeProductFromBasket(@PathVariable String basketName, @PathVariable String productName) {
        motorcycleShopService.deleteProductFromBasket(basketName, productName);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/basket/{name}")
    public ResponseEntity<Void> removeBasket(@PathVariable String name) {
        motorcycleShopService.deleteBasket(name);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/product/toBasket/{basketName}/{productName}")
    public ResponseEntity<Void> addProductToBasket(@PathVariable String basketName, @PathVariable String productName) {
        motorcycleShopService.addProductToBasket(basketName, productName);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/products/fromBasket/{basketName}")
    public ResponseEntity<List<Product>> getAllProductsFromBasket(@PathVariable String basketName) {
        return ResponseEntity.ok().body(motorcycleShopService.getALlProductsFromBasket(basketName));
    }

    @PostMapping("/order")
    public ResponseEntity<OrderCartDTO> addOrder(@RequestBody OrderCartDTO orderDTO) {
        return new  ResponseEntity<>(motorcycleShopService.addOrder(orderDTO), HttpStatus.CREATED);
    }

}
