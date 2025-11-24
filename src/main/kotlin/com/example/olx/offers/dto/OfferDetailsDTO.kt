package com.example.olx.offers.dto

import java.time.Instant

data class OfferDetailsDTO(
    val id: Long,
    val title: String,
    val description: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val price: Int,
)
