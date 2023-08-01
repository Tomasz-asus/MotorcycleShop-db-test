package com.example.motorcycleshop.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.motorcycleshop.exceptions.BasketNotFoundException;
import com.example.motorcycleshop.exceptions.UserAlreadyExistException;
import com.example.motorcycleshop.exceptions.UserNotFoundException;
import com.example.motorcycleshop.model.AppUser;
import com.example.motorcycleshop.model.Basket;
import com.example.motorcycleshop.model.Role;
import com.example.motorcycleshop.repository.AppUserRepository;
import com.example.motorcycleshop.repository.BasketRepository;
import com.example.motorcycleshop.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@Transactional

public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private JavaMailSender mailSender;
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final BasketRepository basketRepository;


    public AppUserServiceImpl(AppUserRepository appUserRepository,
                              RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder,
                              BasketRepository basketRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.basketRepository = basketRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User " + username + " was not found."));
        System.out.println("User found");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles()
                .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }

    @Override
    public void registerUser(AppUser user, String siteUR) throws MessagingException, UnsupportedEncodingException {
        if (appUserRepository.findByUsername(user.getUsername()).isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.getRoles().add(roleRepository.findByName("ROLE_USER"));
            String basketCustomName = UUID.randomUUID().toString().substring(0, 20);
            Basket basket = new Basket(basketCustomName);
            basketRepository.save(basket);
            user.setBasket(basketRepository.findByBasketName(basketCustomName).get());
            String randomCode = UUID.randomUUID().toString().substring(0, 32);
            user.setVerificationCode(randomCode);
            sendVerificationEmail(user, siteUR);
            appUserRepository.save(user);
        } else {
            throw new UserAlreadyExistException("User " + user.getUsername() + " already exist.");
        }
    }

    @Override
    public AppUser saveAdmin(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(roleRepository.findByName("ROLE_ADMIN"));
        String basketCustomName = UUID.randomUUID().toString().substring(0, 20);
        Basket basket = new Basket(basketCustomName);
        basketRepository.save(basket);
        user.setBasket(basketRepository.findByBasketName(basketCustomName).orElseThrow(() ->
                new BasketNotFoundException("Basket " + basketCustomName + " was not found.")));
        user.setVerified(true);
        return appUserRepository.save(user);
    }

    @Override
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                AppUser user = getAppUser(username);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("basketName", findBasketName(user.getUsername()))
                        .withClaim("name", findName(user.getUsername()))
                        .withClaim("isVerified", isUserVerified(user.getUsername()))
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception exception) {

                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());

                Map<String, String> error = new HashMap<>();
                error.put("access_token", exception.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh Token is Missing");
        }
    }

    private void sendVerificationEmail(AppUser user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getUsername();
        String fromAddress = "tomaszojava@gmail.com";
        String senderName = "MotorcycleShop";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "MotorcycleShop.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getName());
        String verifyURL = siteURL + "/api/verify?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);

    }

    @Override
    public Role saveRole(Role role) {
        String roleName = role.getName();

        Role existingRole = roleRepository.findByName(role.getName());

        if (existingRole != null) {
            throw new IllegalArgumentException("Role with name" + roleName + " is exist");
        }
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser user = appUserRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException("User " + username + " was not found."));
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public AppUser getAppUser(String username) {
        return appUserRepository.findByUsername(username).orElseThrow(()->
                new UserNotFoundException("User " + username + " was not found."));
    }

    @Override
    public List<AppUser> getAppUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public boolean verify(String verificationCode) {
        AppUser appUser = appUserRepository.findByVerificationCode(verificationCode);

        if (appUser == null || appUser.isVerified()) {
            return false;
        } else {
            appUser.setVerificationCode(passwordEncoder.encode(appUser.getPassword()));
            appUser.setVerificationCode(null);
            appUser.setVerified(true);
            appUserRepository.save(appUser);
            return true;
        }
    }

    private Boolean isUserVerified(String username) {
        AppUser user = this.appUserRepository.findByUsername(username).orElseThrow();
        return user.isVerified();
    }

    String findBasketName(String username) {
        AppUser user = this.appUserRepository.findByUsername(username).orElseThrow();
        return user.getBasket().getBasketName();
    }

    String findName(String username) {
        AppUser user = this.appUserRepository.findByUsername(username).orElseThrow();
        return user.getName();
    }
}
