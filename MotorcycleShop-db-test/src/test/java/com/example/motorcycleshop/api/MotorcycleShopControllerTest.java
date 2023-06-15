package com.example.motorcycleshop.api;

import com.example.motorcycleshop.DTO.ProductDTO;
import com.example.motorcycleshop.DTO.OrderCartDTO;
import com.example.motorcycleshop.model.AppUser;
import com.example.motorcycleshop.model.Basket;
import com.example.motorcycleshop.model.Product;
import com.example.motorcycleshop.model.ProductCategory;
import com.example.motorcycleshop.repository.AppUserRepository;
import com.example.motorcycleshop.repository.BasketRepository;
import com.example.motorcycleshop.repository.ProductRepository;
import com.example.motorcycleshop.service.ProductMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class MotorcycleShopControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private AppUserRepository appUserRepository;


    @Test
    public void shouldGetAllProducts() throws Exception {
        //GIVEN
        productRepository.save(new Product("Yamaha", "yamaha", 1., "link", ProductCategory.SPORT));
        productRepository.save(new Product("Yamaha", "yamaha", 1., "link", ProductCategory.SPORT));

        //WHEN
        MvcResult mvcResult = this.mockMvc.perform(get("/shop/products")).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString();
        List<ProductDTO> products = Arrays.asList(objectMapper.readValue(contentAsString, ProductDTO[].class));

        //THEN

        assertThat(products.size()).isEqualTo(5);
    }

    @Test
    public void shouldGetTheSameProductsAsGiven() throws Exception {
        //GIVEN
        productRepository.save(new Product("Yamaha", "yamaha", 1., "link", ProductCategory.SPORT));
        productRepository.save(new Product("Yamaha", "yamaha", 1., "link", ProductCategory.SPORT));

        //WHEN
        MvcResult mvcResult = this.mockMvc.perform(get("/shop/products")).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString();
        List<ProductDTO> products = Arrays.asList(objectMapper.readValue(contentAsString, ProductDTO[].class));

        //THEN
        assertThat(products).containsAnyOf(
                new ProductDTO("Yamaha", "yamaha", 1., "link", ProductCategory.SPORT),
                new ProductDTO("Yamaha", "yamaha", 1., "link", ProductCategory.SPORT));
    }

    @Test
    public void shouldAddProductToDatabase() throws Exception {
        //GIVEN
        ProductDTO productDTO =
                new ProductDTO("Yamaha", "yamaha", 1., "link", ProductCategory.SPORT);
        String json = objectMapper.writeValueAsString(productDTO);

        //WHEN
        MvcResult mvcResult = this.mockMvc.perform(post("/shop/product")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();

        //THEN
        assertThat(status).isEqualTo(201);
        assertThat(ProductMapper.fromEntity(productRepository.findByProductName("Yamaha").get())).isEqualTo(productDTO);
    }

    @Test
    public void shouldAddProductToBasket() throws Exception {
        //GIVEN
        productRepository.save(new Product("Yamaha", "yamaha", 1., "link", ProductCategory.SPORT));
        basketRepository.save(new Basket("testBasket"));

        //WHEN
        MvcResult mvcResult = this.mockMvc.perform(post("/shop/product/toBasket/testBasket/Yamaha")).andReturn();
        int status = mvcResult.getResponse().getStatus();
        int productListSize = basketRepository.findByBasketName("testBasket").get().getProducts().size();

        //THEN
        assertThat(status).isEqualTo(202);
        assertThat(productListSize).isEqualTo(1);
    }


    @Test
    public void shouldRemoveProductFromBasket() throws Exception {
        //GIVEN
        Product product = new Product("Yamaha", "yamaha", 1., "link", ProductCategory.SPORT);
        Basket basket = new Basket("testBasket");
        basket.addProductToBasket(product);
        basketRepository.save(basket);

        //
        MvcResult mvcResult = this.mockMvc.perform(delete("/shop/product/fromBasket/testBasket/Yamaha")).andReturn();
        int status = mvcResult.getResponse().getStatus();
        int size = basketRepository.findByBasketName("testBasket").get().getProducts().size();

        //THEN

        assertThat(status).isEqualTo(202);
        assertThat(size).isEqualTo(0);
    }
}
