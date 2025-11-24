package com.example.olx.offers.mapper

import com.example.olx.offers.dto.OfferRequestDTO
import com.example.olx.offers.entity.Offer

fun OfferRequestDTO.toEntity(): Offer =
    Offer(
        id = null,
        title = this.title!!,
        description = this.description!!,
        createdAt = null,
        updatedAt = null,
        price = this.price!!,
    )
