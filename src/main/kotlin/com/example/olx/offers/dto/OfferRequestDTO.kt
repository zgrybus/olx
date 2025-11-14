package com.example.olx.offers.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class OfferRequestDTO(
    @get:NotBlank(message = "Title is required")
    val title: String?,
    @get:NotBlank(message = "Description is required")
    val description: String?,
    @get:NotNull(message = "Price is required")
    @get:Min(value = 0, message = "Price cannot be negative")
    val price: Int?
)
