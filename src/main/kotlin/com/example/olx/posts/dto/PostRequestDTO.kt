package com.example.olx.posts.dto

import jakarta.validation.constraints.NotBlank

data class PostRequestDTO(
    @get:NotBlank(message = "Title is required")
    val title: String?,
    @get:NotBlank(message = "Description is required")
    val description: String?
)
