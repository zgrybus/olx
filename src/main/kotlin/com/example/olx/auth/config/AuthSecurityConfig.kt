package com.example.olx.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class AuthSecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/api/auth/**")
            .csrf {
                it.disable()
            }.authorizeHttpRequests { auth ->
                auth
                    .anyRequest()
                    .permitAll()
            }

        return http.build()
    }
}
