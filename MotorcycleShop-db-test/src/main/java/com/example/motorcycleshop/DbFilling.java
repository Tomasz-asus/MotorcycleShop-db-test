package com.example.motorcycleshop;
import com.example.motorcycleshop.model.AppUser;
import com.example.motorcycleshop.service.AppUserServiceImpl;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Profile("dev")
public class DbFilling {

    private final AppUserServiceImpl appUserService;

    public DbFilling(AppUserServiceImpl appUserService) {
        this.appUserService = appUserService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {

        AppUser adminUser = new AppUser("admin", "admin", "admin", new ArrayList<>());
        appUserService.saveAdmin(adminUser);
    }

}
