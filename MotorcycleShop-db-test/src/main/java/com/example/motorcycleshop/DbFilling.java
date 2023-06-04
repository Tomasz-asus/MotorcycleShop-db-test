package com.example.motorcycleshop;

import com.example.motorcycleshop.DTO.ProductDTO;
import com.example.motorcycleshop.api.MotorcycleShopController;
import com.example.motorcycleshop.model.AppUser;
import com.example.motorcycleshop.model.ProductCategory;
import com.example.motorcycleshop.model.Role;
import com.example.motorcycleshop.repository.RoleRepository;
import com.example.motorcycleshop.service.AppUserServiceImpl;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Profile("prod")
public class DbFilling {

    private final MotorcycleShopController controller;
    private final AppUserServiceImpl appUserService;
    private final RoleRepository roleRepository;

    public DbFilling(MotorcycleShopController controller, AppUserServiceImpl appUserService, RoleRepository roleRepository) {
        this.controller = controller;
        this.appUserService = appUserService;
        this.roleRepository = roleRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {

//        controller.addProduct(new ProductDTO("KTM 1290", "Mini KTM 690", 99.00,
//                "https://motomoda24.pl/wp-content/uploads/2022/06/ktm-690.jpg",
//                ProductCategory.SPORT));
//        controller.addProduct(new ProductDTO("Kawasaki Z 900 RS", "Mini Kawasaki Z 900 RS", 99.00,
//                "https://motomoda24.pl/wp-content/uploads/2019/10/product_d_2_d2-maisto-fertigmodell-kawasaki-z900rs-1-12-10013352_520_20.jpg",
//                ProductCategory.SPORT));
//        controller.addProduct(new ProductDTO("KTM 690", "Mini KTM 690", 99.00,
//                "https://motomoda24.pl/wp-content/uploads/2021/10/maisto-model-ktm-690.jpg",
//                ProductCategory.SPORT));


        Role admin = new Role("ROLE_ADMIN");
        Role user = new Role("ROLE_USER");
        roleRepository.save(admin);
        roleRepository.save(user);

        AppUser adminUser = new AppUser("admin", "admin", "admin", new ArrayList<>());
        appUserService.saveAdmin(adminUser);
    }
}

