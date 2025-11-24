package com.example.olx.offers.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class OfferRequestDTO(
    @field:NotBlank(message = "Title is required")
    @field:Size(min = 15, message = "The title is too short. Please write at least 14 characters.")
    @field:Size(max = 70, message = "The title is too long. Limit is 70 characters.")
    val title: String?,
    @field:NotBlank(message = "Description is required")
    @field:Size(min = 40, message = "The description is too short. Please write at least 40 characters.")
    @field:Size(max = 9000, message = "The description is too long. Limit is 9000 characters.")
    val description: String?,
    @field:NotNull(message = "Price is required")
    @field:Min(value = 0, message = "Price cannot be negative")
    val price: Int?,
)
