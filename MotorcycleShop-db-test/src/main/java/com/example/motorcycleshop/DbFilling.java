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

        AppUser adminUser = new AppUser("admin", "admin", "admin", new ArrayList<>());
        appUserService.saveAdmin(adminUser);
    }

}
