package com.example.olx.offers.dto

import jakarta.validation.constraints.NotBlank

data class OfferRequestDTO(
    @get:NotBlank(message = "Title is required")
    val title: String?,
    @get:NotBlank(message = "Description is required")
    val description: String?
)
