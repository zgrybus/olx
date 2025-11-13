package com.example.olx.offers.mapper

import com.example.olx.offers.dto.OfferDetailsDTO
import com.example.olx.offers.dto.OfferSummaryDTO
import com.example.olx.offers.entity.Post


fun Post.toSummaryDTO(): OfferSummaryDTO = OfferSummaryDTO(
    id = this.id!!,
    title = this.title,
    description = this.description,
    createdAt = this.createdAt!!,
    updatedAt = this.updatedAt!!
)

fun Post.toDetailsDTO(): OfferDetailsDTO = OfferDetailsDTO(
    id = this.id!!,
    title = this.title,
    description = this.description,
    createdAt = this.createdAt!!,
    updatedAt = this.updatedAt!!
)