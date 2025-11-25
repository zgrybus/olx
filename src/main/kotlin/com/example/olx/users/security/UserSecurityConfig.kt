package com.example.olx.users.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class UserSecurityConfig {
    @Bean
    fun userSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/api/users/**")
            .csrf {
                it.disable()
            }.authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.POST, "/api/users")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }

        return http.build()
    }
}
