package com.micro.gateway.SecurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http.csrf(c -> c.disable())
                .authorizeExchange(auth -> auth

                        // ADMIN ROUTES
                        .pathMatchers("/api/products/admin/**").hasRole("ADMIN")
                        .pathMatchers("/api/category/admin/**").hasRole("ADMIN")

                        // PUBLIC ROUTES
                        .pathMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/category/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/users/register").permitAll()

                        // REST ROUTES
                        .anyExchange().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(j -> j.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();

    }

    // convertor for ROLES -> cauz keycloak sends me nested object that has roles
    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess == null || !realmAccess.containsKey("roles")) {
                return Flux.empty();
            }
            List<String> roles = (List<String>) realmAccess.get("roles");
            return Flux.fromIterable(roles).map(
                    role -> new SimpleGrantedAuthority("ROLE_" + role));

        });

        return converter;
    }

}
