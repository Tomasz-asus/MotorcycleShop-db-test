package com.example.motorcycleshop.service;

import com.example.motorcycleshop.DTO.ProductDTO;

import com.example.motorcycleshop.exceptions.BasketNotFoundException;
import com.example.motorcycleshop.exceptions.ProductAlreadyExistException;
import com.example.motorcycleshop.exceptions.ProductNotFoundException;
import com.example.motorcycleshop.mapper.ProductMapper;
import com.example.motorcycleshop.model.Basket;
import com.example.motorcycleshop.model.Product;
import com.example.motorcycleshop.repository.BasketRepository;
import com.example.motorcycleshop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional

public class MotorcycleShopServiceImpl implements MotorcycleShopService {

    private final ProductRepository productRepository;
    private final BasketRepository basketRepository;

    public MotorcycleShopServiceImpl(ProductRepository productRepository,
                                     BasketRepository basketRepository) {
        this.productRepository = productRepository;
        this.basketRepository = basketRepository;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO addProduct(ProductDTO productDTO) {
        if (productRepository
                .findByProductName(productDTO.getProductName())
                .isPresent()) {
            throw new ProductAlreadyExistException("Product Already Exist");
        } else {
            Product save = productRepository.save(ProductMapper.fromDTO(productDTO));
            return ProductMapper.toDTO(save);
        }
    }

    public void deleteProduct(String name) {
        productRepository.findByProductName(name).orElseThrow(() ->
                new ProductNotFoundException("Product not found"));
        productRepository.deleteByProductName(name);
    }

    public List<Basket> getAllBaskets() {
        return basketRepository.findAll();
    }

    public void deleteBasket(String name) {
        basketRepository.deleteByBasketName(name);
    }

    public void deleteProductFromBasket(String basket, String productName) {
        Basket basketEntity = basketRepository.findByBasketName(basket).orElseThrow(()
                -> new BasketNotFoundException("Basket: " + basket + ", was not found"));
        Product byProductName = productRepository.findByProductName(productName).orElseThrow(()
                -> new ProductNotFoundException("Product: " + productName + " is not present in database."));
        if (!basketEntity.getProducts().contains(byProductName)) {
            throw new ProductNotFoundException("Product: " + productName + " is not present.");
        }
        basketEntity.removeProductFromBasket(byProductName);
        basketRepository.save(basketEntity);
    }

    public void addProductToBasket(String basketName, String productName) {
        Basket basket = basketRepository.findByBasketName(basketName).orElseThrow(()
                -> new BasketNotFoundException("Basket" + basketName + "was not found"));
        Product product = productRepository.findByProductName(productName).orElseThrow(
                () -> new ProductNotFoundException("Product: " + productName + ", was not found"));
        basket.getProducts().add(product);
        basketRepository.save(basket);
    }

    public List<Product> getALlProductsFromBasket(String basketName) {
        return basketRepository.findByBasketName(basketName)
                .orElseThrow(() -> new BasketNotFoundException("Basket: " + basketName + ", was not found."))
                .getProducts();
    }
}
