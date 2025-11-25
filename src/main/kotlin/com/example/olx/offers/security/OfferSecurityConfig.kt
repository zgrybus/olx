package com.example.olx.offers.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class OfferSecurityConfig {
    @Bean
    fun offerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/api/offers")
            .csrf {
                it.disable()
            }.authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.GET, "/api/offers", "/api/offers/*")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/offers")
                    .authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/offers/*")
                    .authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/offers/*")
                    .authenticated()
                    .anyRequest()
                    .authenticated()
            }

        return http.build()
    }
}
