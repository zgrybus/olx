package com.example.olx.offers.controller

import com.example.olx.BaseIntegrationTest
import com.example.olx.exceptions.dto.ErrorDTO
import com.example.olx.exceptions.dto.ErrorResponse
import com.example.olx.exceptions.dto.ErrorType
import com.example.olx.offers.dto.OfferDetailsDTO
import com.example.olx.offers.dto.OfferRequestDTO
import com.example.olx.offers.dto.OfferSummaryDTO
import com.example.olx.offers.entity.Offer
import com.example.olx.offers.mapper.toDetailsDTO
import com.example.olx.offers.mapper.toSummaryDTO
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
        fun getText(len: Int): String = "0".repeat(len)
        beforeEach {
            offerRepository.deleteAll()
        }

        describe("GET /api/offers") {
            it("gets all offers") {
                val offers =
                    offerRepository.saveAll(
                        listOf(
                            Offer(title = "offer_title_1", description = "offer_description_1", price = 10),
                            Offer(title = "offer_title_2", description = "offer_description_2", price = 20),
                        ),
                    )

                val response =
                    mockMvc
                        .get("/api/offers")
                        .andReturn()
                        .response

                val returnedOffers =
                    objectMapper.readValue(response.contentAsString, Array<OfferSummaryDTO>::class.java)

                response.status.shouldBe(HttpStatus.OK.value())
                returnedOffers[0].shouldBe(offers[0].toSummaryDTO())
                returnedOffers[1].shouldBe(offers[1].toSummaryDTO())
            }

            it("gets empty list, when there are no offers") {
                val response =
                    mockMvc
                        .get("/api/offers")
                        .andExpect { status { isOk() } }
                        .andReturn()
                        .response

                val returnedOffers =
                    objectMapper.readValue(response.contentAsString, Array<OfferSummaryDTO>::class.java)

                response.status.shouldBe(HttpStatus.OK.value())
                returnedOffers
                    .count()
                    .shouldBe(0)
            }
        }

        describe("GET /api/offers/{id}") {
            it("gets offer by id") {
                val offers =
                    offerRepository.saveAll(
                        listOf(
                            Offer(title = "offer_title_1", description = "offer_description_1", price = 10),
                            Offer(title = "offer_title_2", description = "offer_description_2", price = 20),
                        ),
                    )
                val offerToGet = offers[0]
                val response =
                    mockMvc
                        .get("/api/offers/${offerToGet.id}")
                        .andReturn()
                        .response

                val returnedOffer = objectMapper.readValue(response.contentAsString, OfferDetailsDTO::class.java)

                response.status.shouldBe(HttpStatus.OK.value())
                returnedOffer.shouldBe(offerToGet.toDetailsDTO())
            }

            it("returns an error, when offer is not found") {
                val offerId = 1
                val response =
                    mockMvc
                        .get("/api/offers/$offerId")
                        .andReturn()
                        .response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)
                val expectedError =
                    ErrorResponse(
                        status = HttpStatus.NOT_FOUND.value(),
                        path = "uri=/api/offers/$offerId",
                        errors =
                            listOf(
                                ErrorDTO(type = ErrorType.NOT_FOUND, message = "Offer with $offerId is not found"),
                            ),
                    )

                response.status.shouldBe(HttpStatus.NOT_FOUND.value())
                returnedError.shouldBe(expectedError)
            }
        }

        describe("POST /api/offers") {
            it("adds new offer") {
                val requestedOffer =
                    OfferRequestDTO(
                        title = getText(15),
                        description = getText(40),
                        price = 10,
                    )

                val response =
                    mockMvc
                        .post("/api/offers") {
                            contentType = MediaType.APPLICATION_JSON
                            content = objectMapper.writeValueAsString(requestedOffer)
                        }.andReturn()
                        .response

                val returnedOffer = objectMapper.readValue(response.contentAsString, OfferDetailsDTO::class.java)

                response.status.shouldBe(HttpStatus.CREATED.value())
                with(returnedOffer) {
                    title.shouldBe(requestedOffer.title)
                    description.shouldBe(requestedOffer.description)
                    price.shouldBe(requestedOffer.price)
                    id.shouldNotBeNull()
                    createdAt.shouldBeBefore(Instant.now())
                    updatedAt.shouldBeBefore(Instant.now())
                }

                val offerFromDb =
                    offerRepository
                        .findById(returnedOffer.id)
                        .get()

                returnedOffer.shouldBe(offerFromDb.toDetailsDTO())
            }

            describe("Validation") {

                describe("Title") {
                    it("gets an error, when title is not set") {
                        val requestedOffer =
                            OfferRequestDTO(title = null, description = getText(40), price = 10)

                        val response =
                            mockMvc
                                .post("/api/offers") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(requestedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers",
                                errors =
                                    listOf(
                                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Title is required"),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }

                    it("gets an error, when title is too short (< 15 chars)") {
                        val requestedOffer =
                            OfferRequestDTO(title = getText(14), description = getText(40), price = 10)

                        val response =
                            mockMvc
                                .post("/api/offers") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(requestedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers",
                                errors =
                                    listOf(
                                        ErrorDTO(
                                            type = ErrorType.NOT_VALID,
                                            message = "The title is too short. Please write at least 14 characters.",
                                        ),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }

                    it("gets an error, when title is too long (> 70 chars)") {
                        val requestedOffer =
                            OfferRequestDTO(title = getText(71), description = getText(40), price = 10)

                        val response =
                            mockMvc
                                .post("/api/offers") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(requestedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers",
                                errors =
                                    listOf(
                                        ErrorDTO(
                                            type = ErrorType.NOT_VALID,
                                            message = "The title is too long. Limit is 70 characters.",
                                        ),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }
                }

                describe("Description") {
                    it("gets an error, when description is not set") {
                        val requestedOffer =
                            OfferRequestDTO(title = getText(15), description = null, price = 10)

                        val response =
                            mockMvc
                                .post("/api/offers") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(requestedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers",
                                errors =
                                    listOf(
                                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Description is required"),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }

                    it("gets an error, when description is too short (< 40 chars)") {
                        val requestedOffer =
                            OfferRequestDTO(title = getText(15), description = getText(39), price = 10)

                        val response =
                            mockMvc
                                .post("/api/offers") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(requestedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers",
                                errors =
                                    listOf(
                                        ErrorDTO(
                                            type = ErrorType.NOT_VALID,
                                            message = "The description is too short. Please write at least 40 characters.",
                                        ),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }

                    it("gets an error, when description is too long (> 9000 chars)") {
                        val requestedOffer =
                            OfferRequestDTO(title = getText(15), description = getText(9001), price = 10)

                        val response =
                            mockMvc
                                .post("/api/offers") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(requestedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers",
                                errors =
                                    listOf(
                                        ErrorDTO(
                                            type = ErrorType.NOT_VALID,
                                            message = "The description is too long. Limit is 9000 characters.",
                                        ),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }
                }

                describe("Price") {
                    it("gets an error, when price is not set") {
                        val requestedOffer =
                            OfferRequestDTO(title = getText(15), description = getText(40), price = null)

                        val response =
                            mockMvc
                                .post("/api/offers") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(requestedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers",
                                errors =
                                    listOf(
                                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Price is required"),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }

                    it("gets an error, when price is lower than 0") {
                        val requestedOffer =
                            OfferRequestDTO(title = getText(15), description = getText(40), price = -1)

                        val response =
                            mockMvc
                                .post("/api/offers") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(requestedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers",
                                errors =
                                    listOf(
                                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Price cannot be negative"),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }
                }
            }
        }

        describe("DELETE /api/offers/{id}") {
            it("deletes offer by id") {
                val offers =
                    offerRepository.saveAll(
                        listOf(
                            Offer(title = "offer_title_1", description = "offer_description_1", price = 10),
                            Offer(title = "offer_title_2", description = "offer_description_2", price = 20),
                        ),
                    )
                val removedOffer = offers[0]
                val notRemovedOffer = offers[1]

                val response =
                    mockMvc
                        .delete("/api/offers/${removedOffer.id}")
                        .andReturn()
                        .response

                response.status.shouldBe(HttpStatus.NO_CONTENT.value())

                val offersFromDb = offerRepository.findAll()

                offersFromDb
                    .count()
                    .shouldBe(1)
                with(offersFromDb[0]) {
                    id.shouldBe(notRemovedOffer.id)
                    title.shouldBe(notRemovedOffer.title)
                    description.shouldBe(notRemovedOffer.description)
                    price.shouldBe(notRemovedOffer.price)
                    createdAt.shouldBe(notRemovedOffer.createdAt)
                    updatedAt.shouldBe(notRemovedOffer.updatedAt)
                }
            }

            it("returns an error, when offer is not found") {
                val offerId = 1
                val response =
                    mockMvc
                        .delete("/api/offers/$offerId")
                        .andReturn()
                        .response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)
                val expectedError =
                    ErrorResponse(
                        status = HttpStatus.NOT_FOUND.value(),
                        path = "uri=/api/offers/$offerId",
                        errors =
                            listOf(
                                ErrorDTO(type = ErrorType.NOT_FOUND, message = "Offer with $offerId is not found"),
                            ),
                    )

                response.status.shouldBe(HttpStatus.NOT_FOUND.value())
                returnedError.shouldBe(expectedError)
            }
        }

        describe("PUT /api/offers/{id}") {
            it("updates offer by id") {
                val offers =
                    offerRepository.saveAll(
                        listOf(
                            Offer(title = "${getText(15)}_title_1", description = "${getText(40)}_desc_1", price = 10),
                            Offer(title = "${getText(15)}_title_2", description = "${getText(40)}_desc_2", price = 20),
                        ),
                    )
                val offerToUpdate = offers[0]
                val updatedOffer =
                    OfferRequestDTO(
                        title = "${getText(15)}_updated_title_1",
                        description = "${getText(40)}_updated_desc_2",
                        price = 30,
                    )

                val response =
                    mockMvc
                        .put("/api/offers/${offerToUpdate.id}") {
                            contentType = MediaType.APPLICATION_JSON
                            content = objectMapper.writeValueAsString(updatedOffer)
                        }.andReturn()
                        .response

                val returnedOffer = objectMapper.readValue(response.contentAsString, OfferDetailsDTO::class.java)

                response.status.shouldBe(HttpStatus.OK.value())
                with(returnedOffer) {
                    id.shouldBe(offerToUpdate.id)
                    title.shouldBe(updatedOffer.title)
                    description.shouldBe(updatedOffer.description)
                    price.shouldBe(30)
                    createdAt.shouldBe(offerToUpdate.createdAt)
                    updatedAt
                        .shouldNotBe(offerToUpdate.updatedAt)
                        .shouldBeBefore(Instant.now())
                }

                val updatedOfferFromDb =
                    offerRepository
                        .findById(offerToUpdate.id!!)
                        .get()

                with(updatedOfferFromDb) {
                    id.shouldBe(offerToUpdate.id)
                    title.shouldBe(updatedOffer.title)
                    description.shouldBe(updatedOffer.description)
                    price.shouldBe(30)
                    createdAt.shouldBe(offerToUpdate.createdAt)
                    updatedAt!!
                        .shouldNotBe(offerToUpdate.updatedAt)
                        .shouldBeBefore(Instant.now())
                }
            }

            it("returns an error, when offer is not found") {
                val offerId = 1
                val response =
                    mockMvc
                        .delete("/api/offers/$offerId")
                        .andReturn()
                        .response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)
                val expectedError =
                    ErrorResponse(
                        status = HttpStatus.NOT_FOUND.value(),
                        path = "uri=/api/offers/$offerId",
                        errors =
                            listOf(
                                ErrorDTO(type = ErrorType.NOT_FOUND, message = "Offer with $offerId is not found"),
                            ),
                    )

                response.status.shouldBe(HttpStatus.NOT_FOUND.value())
                returnedError.shouldBe(expectedError)
            }

            describe("Validation") {

                describe("Title") {
                    it("returns an error, when title is not set") {
                        val offers =
                            offerRepository.saveAll(
                                listOf(
                                    Offer(
                                        title = "${getText(15)}_title_1",
                                        description = "${getText(40)}_desc_1",
                                        price = 10,
                                    ),
                                    Offer(
                                        title = "${getText(15)}_title_2",
                                        description = "${getText(40)}_desc_2",
                                        price = 20,
                                    ),
                                ),
                            )
                        val offerToUpdate = offers[0]
                        val updatedOffer =
                            OfferRequestDTO(
                                title = null,
                                description = "${getText(40)}_updated_desc_2",
                                price = 30,
                            )

                        val response =
                            mockMvc
                                .put("/api/offers/${offerToUpdate.id}") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(updatedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers/${offerToUpdate.id}",
                                errors =
                                    listOf(
                                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Title is required"),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }

                    it("returns an error, when title is too short (< 15 chars)") {
                        val offers =
                            offerRepository.saveAll(
                                listOf(
                                    Offer(
                                        title = "${getText(15)}_title_1",
                                        description = "${getText(40)}_desc_1",
                                        price = 10,
                                    ),
                                    Offer(
                                        title = "${getText(15)}_title_2",
                                        description = "${getText(40)}_desc_2",
                                        price = 20,
                                    ),
                                ),
                            )
                        val offerToUpdate = offers[0]
                        val updatedOffer =
                            OfferRequestDTO(
                                title = getText(14),
                                description = "${getText(40)}_updated_desc_2",
                                price = 30,
                            )

                        val response =
                            mockMvc
                                .put("/api/offers/${offerToUpdate.id}") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(updatedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers/${offerToUpdate.id}",
                                errors =
                                    listOf(
                                        ErrorDTO(
                                            type = ErrorType.NOT_VALID,
                                            message = "The title is too short. Please write at least 14 characters.",
                                        ),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }

                    it("returns an error, when title is too long (> 70 chars)") {
                        val offers =
                            offerRepository.saveAll(
                                listOf(
                                    Offer(
                                        title = "${getText(15)}_title_1",
                                        description = "${getText(40)}_desc_1",
                                        price = 10,
                                    ),
                                    Offer(
                                        title = "${getText(15)}_title_2",
                                        description = "${getText(40)}_desc_2",
                                        price = 20,
                                    ),
                                ),
                            )
                        val offerToUpdate = offers[0]
                        val updatedOffer =
                            OfferRequestDTO(
                                title = getText(71),
                                description = "${getText(40)}_updated_desc_2",
                                price = 30,
                            )

                        val response =
                            mockMvc
                                .put("/api/offers/${offerToUpdate.id}") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(updatedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers/${offerToUpdate.id}",
                                errors =
                                    listOf(
                                        ErrorDTO(
                                            type = ErrorType.NOT_VALID,
                                            message = "The title is too long. Limit is 70 characters.",
                                        ),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }
                }

                describe("Description") {
                    it("returns an error, when description is not set") {
                        val offers =
                            offerRepository.saveAll(
                                listOf(
                                    Offer(
                                        title = "${getText(15)}_title_1",
                                        description = "${getText(40)}_desc_1",
                                        price = 10,
                                    ),
                                    Offer(
                                        title = "${getText(15)}_title_2",
                                        description = "${getText(40)}_desc_2",
                                        price = 20,
                                    ),
                                ),
                            )
                        val offerToUpdate = offers[0]
                        val updatedOffer =
                            OfferRequestDTO(
                                title = "${getText(15)}_title_1",
                                description = null,
                                price = 30,
                            )

                        val response =
                            mockMvc
                                .put("/api/offers/${offerToUpdate.id}") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(updatedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers/${offerToUpdate.id}",
                                errors =
                                    listOf(
                                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Description is required"),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }

                    it("returns an error, when description is too short (< 40 chars)") {
                        val offers =
                            offerRepository.saveAll(
                                listOf(
                                    Offer(
                                        title = "${getText(15)}_title_1",
                                        description = "${getText(40)}_desc_1",
                                        price = 10,
                                    ),
                                    Offer(
                                        title = "${getText(15)}_title_2",
                                        description = "${getText(40)}_desc_2",
                                        price = 20,
                                    ),
                                ),
                            )
                        val offerToUpdate = offers[0]
                        val updatedOffer =
                            OfferRequestDTO(
                                title = "${getText(15)}_title_1",
                                description = getText(39),
                                price = 30,
                            )

                        val response =
                            mockMvc
                                .put("/api/offers/${offerToUpdate.id}") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(updatedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers/${offerToUpdate.id}",
                                errors =
                                    listOf(
                                        ErrorDTO(
                                            type = ErrorType.NOT_VALID,
                                            message = "The description is too short. Please write at least 40 characters.",
                                        ),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }

                    it("returns an error, when description is too long (> 9000 chars)") {
                        val offers =
                            offerRepository.saveAll(
                                listOf(
                                    Offer(
                                        title = "${getText(15)}_title_1",
                                        description = "${getText(40)}_desc_1",
                                        price = 10,
                                    ),
                                    Offer(
                                        title = "${getText(15)}_title_2",
                                        description = "${getText(40)}_desc_2",
                                        price = 20,
                                    ),
                                ),
                            )
                        val offerToUpdate = offers[0]
                        val updatedOffer =
                            OfferRequestDTO(
                                title = "${getText(15)}_title_1",
                                description = getText(9001),
                                price = 30,
                            )

                        val response =
                            mockMvc
                                .put("/api/offers/${offerToUpdate.id}") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(updatedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers/${offerToUpdate.id}",
                                errors =
                                    listOf(
                                        ErrorDTO(
                                            type = ErrorType.NOT_VALID,
                                            message = "The description is too long. Limit is 9000 characters.",
                                        ),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }
                }

                describe("Price") {
                    it("returns an error, when price is not set") {
                        val offers =
                            offerRepository.saveAll(
                                listOf(
                                    Offer(
                                        title = "${getText(15)}_title_1",
                                        description = "${getText(40)}_desc_1",
                                        price = 10,
                                    ),
                                    Offer(
                                        title = "${getText(15)}_title_2",
                                        description = "${getText(40)}_desc_2",
                                        price = 20,
                                    ),
                                ),
                            )
                        val offerToUpdate = offers[0]
                        val updatedOffer =
                            OfferRequestDTO(
                                title = "${getText(15)}_title_1",
                                description = "${getText(40)}_description_1",
                                price = null,
                            )

                        val response =
                            mockMvc
                                .put("/api/offers/${offerToUpdate.id}") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(updatedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers/${offerToUpdate.id}",
                                errors =
                                    listOf(
                                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Price is required"),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }

                    it("returns an error, when price is lower than 0") {
                        val offers =
                            offerRepository.saveAll(
                                listOf(
                                    Offer(
                                        title = "${getText(15)}_title_1",
                                        description = "${getText(40)}_desc_1",
                                        price = 10,
                                    ),
                                    Offer(
                                        title = "${getText(15)}_title_2",
                                        description = "${getText(40)}_desc_2",
                                        price = 20,
                                    ),
                                ),
                            )
                        val offerToUpdate = offers[0]
                        val updatedOffer =
                            OfferRequestDTO(
                                title = "${getText(15)}_title_1",
                                description = "${getText(40)}_description_1",
                                price = -1,
                            )

                        val response =
                            mockMvc
                                .put("/api/offers/${offerToUpdate.id}") {
                                    contentType = MediaType.APPLICATION_JSON
                                    content = objectMapper.writeValueAsString(updatedOffer)
                                }.andReturn()
                                .response

                        val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                        val expectedError =
                            ErrorResponse(
                                status = HttpStatus.BAD_REQUEST.value(),
                                path = "uri=/api/offers/${offerToUpdate.id}",
                                errors =
                                    listOf(
                                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Price cannot be negative"),
                                    ),
                            )

                        response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                        returnedError.shouldBe(expectedError)
                    }
                }
            }
        }
    }
}
