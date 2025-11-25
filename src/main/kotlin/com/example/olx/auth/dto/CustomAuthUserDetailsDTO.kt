package com.example.olx.auth.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.UUID

data class CustomAuthUserDetailsDTO(
    val id: UUID,
    val username: String,
    val password: String,
    val authorities: Collection<GrantedAuthority>,
) : User(username, password, authorities)
