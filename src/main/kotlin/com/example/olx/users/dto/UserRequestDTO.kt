package com.example.olx.users.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class UserRequestDTO(
    val id: UUID,
    @field:NotBlank("User is required")
    @field:Size(min = 1, message = "The username is too short. Please write at least 1 character.")
    @field:Size(max = 25, message = "The title is too long. Limit is 25 characters.")
    val username: String,
    @field:NotBlank("Password is required")
    @field:Size(min = 8, message = "The username is too short. Please write at least 8 characters.")
    @field:Size(max = 25, message = "The title is too long. Limit is 25 characters.")
    val password: String,
)
