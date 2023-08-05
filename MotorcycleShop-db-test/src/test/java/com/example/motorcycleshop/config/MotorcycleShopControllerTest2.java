package com.example.motorcycleshop.config;

import com.example.motorcycleshop.DTO.ProductDTO;
import com.example.motorcycleshop.mapper.ProductMapper;
import com.example.motorcycleshop.model.Basket;
import com.example.motorcycleshop.model.Product;
import com.example.motorcycleshop.model.ProductCategory;
import com.example.motorcycleshop.repository.BasketRepository;
import com.example.motorcycleshop.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class MotorcycleShopControllerTest2 {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BasketRepository basketRepository;


    @Test
    public void shouldGetAllProducts() throws Exception {
        //GIVEN
        productRepository.save(new Product("Yamaha", "yamaha", 1., "link", ProductCategory.SPORT));
        productRepository.save(new Product("Yamaha2", "yamaha2", 1., "link2", ProductCategory.SPORT));

        //WHEN
        MvcResult mvcResult = this.mockMvc.perform(get("/shop/products")).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString();
        List<ProductDTO> products = Arrays.asList(objectMapper.readValue(contentAsString, ProductDTO[].class));

        //THEN

        assertThat(products.size()).isGreaterThan(1);
    }

    @Test
    public void shouldGetTheSameProductsAsGiven() throws Exception {
        //GIVEN
        productRepository.save(new Product("Yamaha", "yamaha", 1., "link", ProductCategory.SPORT));
        productRepository.save(new Product("Yamaha2", "yamaha2", 1., "link2", ProductCategory.SPORT));

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
        assertThat(ProductMapper.toDTO(productRepository.findByProductName("Yamaha").get())).isEqualTo(productDTO);
    }

    @Test
    public void shouldAddProductToBasket() throws Exception {
        //GIVEN
        Product product = new Product("Yamaha", "yamaha", 1., "link", ProductCategory.SPORT);
        Basket basket = new Basket("testBasket2");
        basket.addProductToBasket(product);
        basketRepository.save(basket);

        //
        MvcResult mvcResult = this.mockMvc.perform(post("/shop/product/toBasket/testBasket2/Yamaha")).andReturn();
        int status = mvcResult.getResponse().getStatus();
        int size = basketRepository.findByBasketName("testBasket2").get().getProducts().size();

        //THEN

        assertThat(status).isEqualTo(202);
        assertThat(size).isEqualTo(2);
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
