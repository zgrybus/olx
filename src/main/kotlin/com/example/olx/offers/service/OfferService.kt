package com.example.olx.offers.service

import com.example.olx.Loggable
import com.example.olx.offers.dto.OfferDetailsDTO
import com.example.olx.offers.dto.OfferRequestDTO
import com.example.olx.offers.dto.OfferSummaryDTO
import com.example.olx.offers.exceptions.OfferNotFoundException
import com.example.olx.offers.mapper.toDetailsDTO
import com.example.olx.offers.mapper.toEntity
import com.example.olx.offers.mapper.toSummaryDTO
import com.example.olx.offers.repository.OfferRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OfferService(
    val offerRepository: OfferRepository,
) : Loggable {

    @Transactional(readOnly = true)
    fun getAllOffers(): List<OfferSummaryDTO> {
        logger.info { "Attempting to get all offers" }

        return offerRepository.findAll()
            .also {
                logger.info { "Successfully found ${it.size} offers" }
            }
            .let {
                it.map { offer ->
                    offer.toSummaryDTO()
                }
            }
    }

    @Transactional(readOnly = true)
    fun getOfferById(offerId: Long): OfferDetailsDTO {
        logger.info { "Attempting to get offer by id $offerId" }

        return offerRepository.findById(offerId)
            .orElseThrow { OfferNotFoundException("Offer with $offerId is not found") }
            .also {
                logger.info { "Successfully found offer with id $offerId" }
            }
            .toDetailsDTO()
    }

    @Transactional
    fun deleteOfferById(offerId: Long) {
        logger.info { "Attempting to delete offer by id $offerId" }

        offerRepository.findById(offerId)
            .orElseThrow { OfferNotFoundException("Offer with $offerId is not found") }
            .let {
                offerRepository.delete(it)
            }
            .also {
                logger.info { "Successfully found offer with id $offerId" }
            }
    }

    @Transactional
    fun addOffer(offerRequestDTO: OfferRequestDTO): OfferDetailsDTO {
        logger.info { "Attempting to add offer with details: $offerRequestDTO" }

        return offerRepository.save(offerRequestDTO.toEntity())
            .also { logger.info { "Successfully added offer: $it" } }
            .toDetailsDTO()
    }

    @Transactional
    fun updateOffer(offerId: Long, offerRequestDTO: OfferRequestDTO): OfferDetailsDTO {
        logger.info { "Attempting to update offer by id $offerId" }

        val offerToUpdate = offerRepository.findById(offerId)
            .orElseThrow { OfferNotFoundException("Offer with $offerId is not found") }
            .apply {
                title = offerRequestDTO.title!!
                description = offerRequestDTO.description!!
            }

        return offerRepository.saveAndFlush(offerToUpdate)
            .also { logger.info { "Successfully updated offer: $it" } }
            .toDetailsDTO()
    }
}