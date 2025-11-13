package com.example.olx.offers.controller

import com.example.olx.BaseIntegrationTest
import com.example.olx.exceptions.dto.ErrorDTO
import com.example.olx.exceptions.dto.ErrorResponse
import com.example.olx.exceptions.dto.ErrorType
import com.example.olx.offers.dto.OfferDetailsDTO
import com.example.olx.offers.dto.OfferRequestDTO
import com.example.olx.offers.dto.OfferSummaryDTO
import com.example.olx.offers.entity.Offer
import com.example.olx.offers.repository.OfferRepository
import io.kotest.matchers.date.shouldBeBefore
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.Instant

class OfferControllerTest : BaseIntegrationTest() {

    @Autowired
    lateinit var offerRepository: OfferRepository

    init {
        beforeEach {
            offerRepository.deleteAll()
        }

        describe("GET /api/offers") {
            it("gets all offers") {
                offerRepository.saveAll(
                    listOf(
                        Offer(title = "offer_title_1", description = "offer_description_1"),
                        Offer(title = "offer_title_2", description = "offer_description_2")
                    )
                )

                val response = mockMvc.get("/api/offers")
                    .andReturn().response

                val returnedOffers =
                    objectMapper.readValue(response.contentAsString, Array<OfferSummaryDTO>::class.java)

                response.status.shouldBe(HttpStatus.OK.value())
                with(returnedOffers[0]) {
                    id.shouldNotBeNull()
                    title.shouldBe("offer_title_1")
                    description.shouldBe("offer_description_1")
                    createdAt.shouldBeBefore(Instant.now())
                    updatedAt.shouldBeBefore(Instant.now())
                }
                with(returnedOffers[1]) {
                    id.shouldNotBeNull()
                    title.shouldBe("offer_title_2")
                    description.shouldBe("offer_description_2")
                    createdAt.shouldBeBefore(Instant.now())
                    updatedAt.shouldBeBefore(Instant.now())
                }
            }

            it("gets empty list, when there are no offers") {
                val response = mockMvc.get("/api/offers")
                    .andExpect { status { isOk() } }
                    .andReturn().response

                val returnedOffers =
                    objectMapper.readValue(response.contentAsString, Array<OfferSummaryDTO>::class.java)

                response.status.shouldBe(HttpStatus.OK.value())
                returnedOffers.count()
                    .shouldBe(0)
            }
        }

        describe("GET /api/offers/{id}") {
            it("gets offer by id") {
                val offers = offerRepository.saveAll(
                    listOf(
                        Offer(title = "offer_title_1", description = "offer_description_1"),
                        Offer(title = "offer_title_2", description = "offer_description_2")
                    )
                )
                val offerToUpdate = offers[0]
                val response = mockMvc.get("/api/offers/${offerToUpdate.id}")
                    .andReturn().response

                val returnedOffer = objectMapper.readValue(response.contentAsString, OfferDetailsDTO::class.java)

                response.status.shouldBe(HttpStatus.OK.value())
                with(returnedOffer) {
                    id.shouldBe(offerToUpdate.id)
                    title.shouldBe("offer_title_1")
                    description.shouldBe("offer_description_1")
                    createdAt.shouldBe(offerToUpdate.createdAt)
                    updatedAt.shouldBe(offerToUpdate.updatedAt)
                }
            }

            it("returns an error, when offer is not found") {
                val offerId = 1
                val response = mockMvc.get("/api/offers/$offerId")
                    .andReturn().response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)
                val expectedError = ErrorResponse(
                    status = HttpStatus.NOT_FOUND.value(),
                    path = "uri=/api/offers/$offerId",
                    errors = listOf(
                        ErrorDTO(type = ErrorType.NOT_FOUND, message = "Offer with $offerId is not found")
                    )
                )

                response.status.shouldBe(HttpStatus.NOT_FOUND.value())
                returnedError.shouldBe(expectedError)
            }
        }

        describe("POST /api/offers") {
            it("adds new offer") {
                val requestedOffer =
                    OfferRequestDTO(title = "offer_request_title_1", description = "offer_request_description_1")

                val response = mockMvc.post("/api/offers") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(requestedOffer)
                }
                    .andReturn().response

                val returnedOffer = objectMapper.readValue(response.contentAsString, OfferDetailsDTO::class.java)

                response.status.shouldBe(HttpStatus.CREATED.value())
                with(returnedOffer) {
                    id.shouldNotBeNull()
                    title.shouldBe("offer_request_title_1")
                    description.shouldBe("offer_request_description_1")
                    createdAt.shouldBeBefore(Instant.now())
                    updatedAt.shouldBeBefore(Instant.now())
                }

                val offerFromDb = offerRepository.findById(returnedOffer.id)
                    .get()

                with(offerFromDb) {
                    id.shouldBe(returnedOffer.id)
                    title.shouldBe("offer_request_title_1")
                    description.shouldBe("offer_request_description_1")
                    createdAt!!.shouldBeBefore(Instant.now())
                    updatedAt!!.shouldBeBefore(Instant.now())
                }
            }

            it("gets an error, when title is not set") {
                val requestedOffer =
                    OfferRequestDTO(title = null, description = "offer_request_description_1")

                val response = mockMvc.post("/api/offers") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(requestedOffer)
                }
                    .andReturn().response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                val expectedError = ErrorResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    path = "uri=/api/offers",
                    errors = listOf(
                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Title is required")
                    )
                )

                response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                returnedError.shouldBe(expectedError)
            }

            it("gets an error, when description is not set") {
                val requestedOffer =
                    OfferRequestDTO(title = "offer_request_title_1", description = null)

                val response = mockMvc.post("/api/offers") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(requestedOffer)
                }
                    .andReturn().response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                val expectedError = ErrorResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    path = "uri=/api/offers",
                    errors = listOf(
                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Description is required")
                    )
                )

                response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                returnedError.shouldBe(expectedError)
            }
        }

        describe("DELETE /api/offers/{id}") {
            it("deletes offer by id") {
                val offers = offerRepository.saveAll(
                    listOf(
                        Offer(title = "offer_title_1", description = "offer_description_1"),
                        Offer(title = "offer_title_2", description = "offer_description_2")
                    )
                )
                val removedOffer = offers[0]
                val notRemovedOffer = offers[1]

                val response = mockMvc.delete("/api/offers/${removedOffer.id}")
                    .andReturn().response

                response.status.shouldBe(HttpStatus.NO_CONTENT.value())

                val offersFromDb = offerRepository.findAll()

                offersFromDb.count()
                    .shouldBe(1)
                with(offersFromDb[0]) {
                    id.shouldBe(notRemovedOffer.id)
                    title.shouldBe(notRemovedOffer.title)
                    description.shouldBe(notRemovedOffer.description)
                    createdAt.shouldBe(notRemovedOffer.createdAt)
                    updatedAt.shouldBe(notRemovedOffer.updatedAt)
                }
            }

            it("returns an error, when offer is not found") {
                val offerId = 1
                val response = mockMvc.delete("/api/offers/$offerId")
                    .andReturn().response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)
                val expectedError = ErrorResponse(
                    status = HttpStatus.NOT_FOUND.value(),
                    path = "uri=/api/offers/$offerId",
                    errors = listOf(
                        ErrorDTO(type = ErrorType.NOT_FOUND, message = "Offer with $offerId is not found")
                    )
                )

                response.status.shouldBe(HttpStatus.NOT_FOUND.value())
                returnedError.shouldBe(expectedError)
            }
        }

        describe("PUT /api/offers/{id}") {
            it("updates offer by id") {
                val offers = offerRepository.saveAll(
                    listOf(
                        Offer(title = "offer_title_1", description = "offer_description_1"),
                        Offer(title = "offer_title_2", description = "offer_description_2")
                    )
                )
                val offerToUpdate = offers[0]
                val updatedOffer = OfferRequestDTO(title = "updated_title_1", description = "updated_description_1")

                val response = mockMvc.put("/api/offers/${offerToUpdate.id}") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updatedOffer)
                }
                    .andReturn().response

                val returnedOffer = objectMapper.readValue(response.contentAsString, OfferDetailsDTO::class.java)

                response.status.shouldBe(HttpStatus.OK.value())
                with(returnedOffer) {
                    id.shouldBe(offerToUpdate.id)
                    title.shouldBe(updatedOffer.title)
                    description.shouldBe(updatedOffer.description)
                    createdAt.shouldBe(offerToUpdate.createdAt)
                    updatedAt.shouldNotBe(offerToUpdate.updatedAt)
                        .shouldBeBefore(Instant.now())
                }

                val updatedOfferFromDb = offerRepository.findById(offerToUpdate.id!!)
                    .get()

                with(updatedOfferFromDb) {
                    id.shouldBe(offerToUpdate.id)
                    title.shouldBe(updatedOffer.title)
                    description.shouldBe(updatedOffer.description)
                    createdAt.shouldBe(offerToUpdate.createdAt)
                    updatedAt!!.shouldNotBe(offerToUpdate.updatedAt)
                        .shouldBeBefore(Instant.now())
                }
            }

            it("returns an error, when offer is not found") {
                val offerId = 1
                val response = mockMvc.delete("/api/offers/$offerId")
                    .andReturn().response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)
                val expectedError = ErrorResponse(
                    status = HttpStatus.NOT_FOUND.value(),
                    path = "uri=/api/offers/$offerId",
                    errors = listOf(
                        ErrorDTO(type = ErrorType.NOT_FOUND, message = "Offer with $offerId is not found")
                    )
                )

                response.status.shouldBe(HttpStatus.NOT_FOUND.value())
                returnedError.shouldBe(expectedError)
            }

            it("returns an error, when title is not set") {
                val offers = offerRepository.saveAll(
                    listOf(
                        Offer(title = "offer_title_1", description = "offer_description_1"),
                        Offer(title = "offer_title_2", description = "offer_description_2")
                    )
                )
                val offerToUpdate = offers[0]
                val updatedOffer = OfferRequestDTO(title = null, description = "updated_description_1")

                val response = mockMvc.put("/api/offers/${offerToUpdate.id}") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updatedOffer)
                }
                    .andReturn().response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                val expectedError = ErrorResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    path = "uri=/api/offers/${offerToUpdate.id}",
                    errors = listOf(
                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Title is required")
                    )
                )

                response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                returnedError.shouldBe(expectedError)
            }

            it("returns an error, when description is not set") {
                val offers = offerRepository.saveAll(
                    listOf(
                        Offer(title = "offer_title_1", description = "offer_description_1"),
                        Offer(title = "offer_title_2", description = "offer_description_2")
                    )
                )
                val offerToUpdate = offers[0]
                val updatedOffer = OfferRequestDTO(title = "updated_title_1", description = null)

                val response = mockMvc.put("/api/offers/${offerToUpdate.id}") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updatedOffer)
                }
                    .andReturn().response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                val expectedError = ErrorResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    path = "uri=/api/offers/${offerToUpdate.id}",
                    errors = listOf(
                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Description is required")
                    )
                )

                response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                returnedError.shouldBe(expectedError)
            }
        }
    }
}