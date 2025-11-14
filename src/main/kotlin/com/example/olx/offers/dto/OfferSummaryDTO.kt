package com.example.olx.offers.dto

import java.time.Instant

data class OfferSummaryDTO(
    val id: Long,
    val title: String,
    val description: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val price: Int
)
