package com.example.olx.offers.controller

import com.example.olx.offers.dto.OfferDetailsDTO
import com.example.olx.offers.dto.OfferRequestDTO
import com.example.olx.offers.dto.OfferSummaryDTO
import com.example.olx.offers.service.OfferService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/offers")
@Validated
class OfferController(val offerService: OfferService) {

    @GetMapping
    fun getAllOffers(): List<OfferSummaryDTO> = offerService.getAllOffers()

    @GetMapping("/{offerId}")
    fun getOfferById(@PathVariable offerId: Long): OfferDetailsDTO = offerService.getOfferById(offerId)

    @DeleteMapping("/{offerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOfferById(@PathVariable offerId: Long) = offerService.deleteOfferById(offerId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addOffer(@RequestBody @Valid offerRequestDTO: OfferRequestDTO): OfferDetailsDTO =
        offerService.addOffer(offerRequestDTO)

    @PutMapping("/{offerId}")
    fun updatePost(@PathVariable offerId: Long, @RequestBody @Valid offerRequestDTO: OfferRequestDTO): OfferDetailsDTO =
        offerService.updateOffer(offerId, offerRequestDTO)
}