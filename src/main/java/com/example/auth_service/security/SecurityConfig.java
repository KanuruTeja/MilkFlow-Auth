package com.example.auth_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ✅ Swagger endpoints - allow open access
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // ✅ Authentication endpoints - allow open access
                        .requestMatchers("/auth/register", "/auth/login").permitAll()

                        // ✅ Role-based restrictions (for internal APIs)
                        .requestMatchers("/farmer/**").hasRole("FARMER")
                        .requestMatchers("/delivery/**").hasRole("DELIVERY")
                        .requestMatchers("/manager/**").hasRole("MANAGER")
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")

                        // ✅ Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        // 🔥 You were missing this return statement
        return http.build();
    }
}
