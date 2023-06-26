package com.example.motorcycleshop.security;
import com.example.motorcycleshop.repository.AppUserRepository;
import com.example.motorcycleshop.security.filter.CustomAuthenticationFilter;
import com.example.motorcycleshop.security.filter.CustomAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository userRepo;
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, AppUserRepository userRepo, AuthenticationConfiguration authenticationConfiguration) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(userRepo, authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http.csrf().disable();
        http.cors().and();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().requestMatchers("/api/login",
                "/shop/products",
                "/shop/product",
                "/api/token/refresh",
                "/api/user",
                "/api/verify",
                "/console/**").permitAll();
        http.headers().frameOptions().disable();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }
}
