package com.example.olx.offers.mapper

import com.example.olx.offers.dto.OfferDetailsDTO
import com.example.olx.offers.dto.OfferRequestDTO
import com.example.olx.offers.dto.OfferSummaryDTO
import com.example.olx.offers.entity.Offer

fun Offer.toSummaryDTO(): OfferSummaryDTO =
    OfferSummaryDTO(
        id = this.id!!,
        title = this.title,
        description = this.description,
        createdAt = this.createdAt!!,
        updatedAt = this.updatedAt!!,
        price = this.price,
    )

fun Offer.toDetailsDTO(): OfferDetailsDTO =
    OfferDetailsDTO(
        id = this.id!!,
        title = this.title,
        description = this.description,
        createdAt = this.createdAt!!,
        updatedAt = this.updatedAt!!,
        price = this.price,
    )

fun Offer.toUpdateWithRequestDto(offerRequestDTO: OfferRequestDTO): Offer =
    Offer(
        id = this.id,
        title = offerRequestDTO.title!!,
        description = offerRequestDTO.description!!,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        price = offerRequestDTO.price!!,
    )
