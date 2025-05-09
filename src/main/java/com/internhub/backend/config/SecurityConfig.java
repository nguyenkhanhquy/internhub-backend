package com.internhub.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    private static final String[] PUBLIC_POST_ENDPOINTS = {"/auth/**", "/users/register/**", "/users/verify/**", "/jobs"};

    private static final String[] PUBLIC_GET_ENDPOINTS = {"/jobs/**", "/companies/**", "/auth/outbound/authentication", "/academic/**"};

    private static final String[] PUBLIC_ENDPOINTS = {"/", "/files/**", "/ws-demo/**", "/ws/**", "/swagger-ui/**", "/v3/api-docs/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, CustomJwtDecoder customJwtDecoder) throws Exception {
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
                .anyRequest()
                .authenticated()
        );

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigure -> jwtConfigure.decoder(customJwtDecoder))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.addAllowedOrigin("http://localhost:3000");
            corsConfiguration.addAllowedOrigin(allowedOrigins);
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
            corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
            corsConfiguration.setAllowCredentials(true);
            return corsConfiguration;
        }));

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10); // Độ mạnh của BCrypt là 10
    }
}
