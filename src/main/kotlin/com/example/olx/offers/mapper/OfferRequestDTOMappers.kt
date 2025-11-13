package com.example.olx.offers.mapper

import com.example.olx.offers.dto.OfferRequestDTO
import com.example.olx.offers.entity.Post

fun OfferRequestDTO.toEntity(): Post = Post(
    id = null,
    title = this.title!!,
    description = this.description!!,
    createdAt = null,
    updatedAt = null,
)