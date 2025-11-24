package com.example.olx.users.dto

import java.util.UUID

data class UserDetailsDTO(
    val id: UUID,
    val username: String,
    val password: String,
)
