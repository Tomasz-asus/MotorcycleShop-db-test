package com.example.motorcycleshop.api;

import com.example.motorcycleshop.model.AppUser;
import com.example.motorcycleshop.model.Role;
import com.example.motorcycleshop.model.RoleToUserForm;
import com.example.motorcycleshop.service.AppUserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }
    @PostMapping("/user")
    @ResponseBody()
    public ResponseEntity<?> registerUser(@RequestBody AppUser appUser, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        appUserService.registerUser(appUser, getSiteURL(request));
        return ResponseEntity.created(uri).body(JSONObject.quote("User saved"));
    }
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@Param("code") String code) {
        if (appUserService.verify(code)) {
            return ResponseEntity.ok().body("verify_success");
        } else {
            return ResponseEntity.badRequest().body("verify_fail");
        }
    }
    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getAppUsers() {
        return ResponseEntity.ok().body(appUserService.getAppUsers());
    }
    @PostMapping("/role")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(appUserService.saveRole(role));
    }
    @PostMapping("/role/toUser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        appUserService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.appUserService.refresh(request, response);
    }
}


