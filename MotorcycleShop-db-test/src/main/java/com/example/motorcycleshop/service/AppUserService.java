package com.example.motorcycleshop.service;



import com.example.motorcycleshop.model.AppUser;
import com.example.motorcycleshop.model.Role;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface AppUserService {

    void registerUser(AppUser user, String siteUrl) throws MessagingException, UnsupportedEncodingException;
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    AppUser saveAdmin(AppUser user);
    AppUser getAppUser(String username);
    List<AppUser> getAppUsers();
    boolean verify(String verificationCode);
    void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
