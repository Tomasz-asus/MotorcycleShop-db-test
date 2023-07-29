package com.example.motorcycleshop;
//usun nieużywane importy - pilnuj tego przy commitach
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

    // usuń jak nie używasz. W ogóle to cała ta klasa do wywalenia :p Jak bardzo chcesz coś dodać do bazy to lepiej zrobić profil DEV
    // bo na prod to na pewno nie będzie stosowane
    private final MotorcycleShopController controller;
    private final AppUserServiceImpl appUserService;
    // roleRepository najpewniej jest do usunięcia, role pobierzesz z AppUserRepository, nadprodukcja repo to też nie jest dobra praktyka
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
