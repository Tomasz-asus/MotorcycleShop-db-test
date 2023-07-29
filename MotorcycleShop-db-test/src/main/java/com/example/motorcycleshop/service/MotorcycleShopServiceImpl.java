package com.example.motorcycleshop.service;

import com.example.motorcycleshop.DTO.ProductDTO;
import com.example.motorcycleshop.DTO.OrderCartDTO;
import com.example.motorcycleshop.exceptions.BasketNotFoundException;
import com.example.motorcycleshop.exceptions.ProductAlreadyExistException;
import com.example.motorcycleshop.exceptions.ProductNotFoundException;
import com.example.motorcycleshop.exceptions.UserNotFoundException;
import com.example.motorcycleshop.model.AppUser;
import com.example.motorcycleshop.model.Basket;
import com.example.motorcycleshop.model.Product;
import com.example.motorcycleshop.model.OrderCart;
import com.example.motorcycleshop.repository.AppUserRepository;
import com.example.motorcycleshop.repository.BasketRepository;
import com.example.motorcycleshop.repository.ProductRepository;
import com.example.motorcycleshop.repository.OrderCartRepository;
//import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
/*
Za duże masz te serwisy, spróbuje je rozdzielić ze względu na odpowiedzialność, da to większą przejrzystość
 i może będziesz mógł zmiejszyć ilość zależności
*/
public class MotorcycleShopServiceImpl implements MotorcycleShopService {

    private final ProductRepository productRepository;
    private final BasketRepository basketRepository;
    private final OrderCartRepository orderCartRepository;
    private final AppUserRepository appUserRepository;

    public MotorcycleShopServiceImpl(ProductRepository productRepository, BasketRepository basketRepository, OrderCartRepository orderCartRepository, AppUserRepository appUserRepository) {
        this.productRepository = productRepository;
        this.basketRepository = basketRepository;
        this.orderCartRepository = orderCartRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductMapper::fromEntity)
                .collect(Collectors.toList());
    }

    public ProductDTO addProduct(ProductDTO productDTO) {
        if (productRepository
                .findByProductName(productDTO.getProductName())
                .isPresent()) {
            throw new ProductAlreadyExistException("Product Already Exist");
        } else {
            Product save = productRepository.save(ProductMapper.fromDTO(productDTO));
            // może lepiej toDto zamiast fromEntity - ale to pierdoła
            return ProductMapper.fromEntity(save);
        }
    }

    public void deleteProduct(String name) {
        productRepository.findByProductName(name).orElseThrow(() ->
                new ProductNotFoundException("Product not found"));
        productRepository.deleteByProductName(name);
    }

    public void clearProductsList() {
        productRepository.deleteAll();
    }

    public List<Basket> getAllBaskets() {
        return basketRepository.findAll();
    }

    public void addBasket(Basket basket) {
        basketRepository.save(basket);
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

    public OrderCartDTO addOrder(OrderCartDTO orderDTO) {
        OrderCart save = orderCartRepository.save(OrderCartMapper.fromDTO(orderDTO));
        AppUser appUser = appUserRepository.findByUsername(orderDTO.getUsername()).orElseThrow(()
                -> new UserNotFoundException("User " + orderDTO.getUsername() + " was not found."));
        appUser.getOrderCarts().add(save);
        String basketCustomName = UUID.randomUUID().toString().substring(0,20);
        Basket basket = new Basket(basketCustomName);
        basketRepository.save(basket);
        appUser.setBasket(basketRepository.findByBasketName(basketCustomName).orElseThrow(() ->
                new BasketNotFoundException("Basket " +  basketCustomName + " was not found.")));
        appUserRepository.save(appUser);
        return OrderCartMapper.fromEntity(save);
    }
}
