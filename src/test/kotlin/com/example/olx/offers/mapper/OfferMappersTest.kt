package com.example.olx.offers.mapper

import com.example.olx.offers.dto.OfferRequestDTO
import com.example.olx.offers.entity.Offer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.Instant

class OfferMappersTest : DescribeSpec() {
    private val testInstant = Instant.now()
    private val baseOffer =
        Offer(
            id = 1L,
            title = "Test Offer",
            description = "Test Description",
            price = 100,
            createdAt = testInstant,
            updatedAt = testInstant,
        )

    init {
        describe("toSummaryDTO") {
            it("maps Offer to OfferSummaryDTO") {
                with(baseOffer.toSummaryDTO()) {
                    id.shouldBe(baseOffer.id)
                    title.shouldBe(baseOffer.title)
                    description.shouldBe(baseOffer.description)
                    price.shouldBe(baseOffer.price)
                    createdAt.shouldBe(baseOffer.createdAt)
                    updatedAt.shouldBe(baseOffer.updatedAt)
                }
            }

            describe("Exception") {
                it("throws exception when id is null") {
                    val offerWithNull =
                        Offer(
                            id = null,
                            title = "t",
                            description = "d",
                            price = 1,
                            createdAt = testInstant,
                            updatedAt = testInstant,
                        )
                    shouldThrow<NullPointerException> {
                        offerWithNull.toSummaryDTO()
                    }
                }

                it("throws exception when createdAt is null") {
                    val offerWithNull =
                        Offer(
                            id = 1L,
                            title = "t",
                            description = "d",
                            price = 1,
                            createdAt = null,
                            updatedAt = testInstant,
                        )
                    shouldThrow<NullPointerException> {
                        offerWithNull.toSummaryDTO()
                    }
                }

                it("throws exception when updatedAt is null") {
                    val offerWithNull =
                        Offer(
                            id = 1L,
                            title = "t",
                            description = "d",
                            price = 1,
                            createdAt = testInstant,
                            updatedAt = null,
                        )
                    shouldThrow<NullPointerException> {
                        offerWithNull.toSummaryDTO()
                    }
                }
            }
        }

        describe("toDetailsDTO") {
            it("maps Offer to OfferDetailsDTO") {
                with(baseOffer.toDetailsDTO()) {
                    id.shouldBe(baseOffer.id)
                    title.shouldBe(baseOffer.title)
                    description.shouldBe(baseOffer.description)
                    price.shouldBe(baseOffer.price)
                    createdAt.shouldBe(baseOffer.createdAt)
                    updatedAt.shouldBe(baseOffer.updatedAt)
                }
            }

            describe("Exception") {
                it("throws exception when id is null") {
                    val offerWithNull =
                        Offer(
                            id = null,
                            title = "t",
                            description = "d",
                            price = 1,
                            createdAt = testInstant,
                            updatedAt = testInstant,
                        )
                    shouldThrow<NullPointerException> {
                        offerWithNull.toDetailsDTO()
                    }
                }

                it("throws exception when createdAt is null") {
                    val offerWithNull =
                        Offer(
                            id = 1L,
                            title = "t",
                            description = "d",
                            price = 1,
                            createdAt = null,
                            updatedAt = testInstant,
                        )
                    shouldThrow<NullPointerException> {
                        offerWithNull.toDetailsDTO()
                    }
                }

                it("throws exception when updatedAt is null") {
                    val offerWithNull =
                        Offer(
                            id = 1L,
                            title = "t",
                            description = "d",
                            price = 1,
                            createdAt = testInstant,
                            updatedAt = null,
                        )
                    shouldThrow<NullPointerException> {
                        offerWithNull.toDetailsDTO()
                    }
                }
            }
        }

        describe("toUpdateWithRequestDto") {
            val dto =
                OfferRequestDTO(
                    title = "Updated Title",
                    description = "Updated Description",
                    price = 200,
                )

            it("updates an Offer entity with values from a DTO") {
                val updatedOffer = baseOffer.toUpdateWithRequestDto(dto)

                with(updatedOffer) {
                    title.shouldBe(dto.title)
                    description.shouldBe(dto.description)
                    price.shouldBe(dto.price)
                    id.shouldBe(baseOffer.id)
                    createdAt.shouldBe(baseOffer.createdAt)
                    updatedAt.shouldBe(baseOffer.updatedAt)
                }
            }

            describe("Exception") {
                it("throws exception when DTO title is null") {
                    val dtoWithNull = OfferRequestDTO(title = null, description = "d", price = 1)
                    shouldThrow<NullPointerException> {
                        baseOffer.toUpdateWithRequestDto(dtoWithNull)
                    }
                }

                it("throws exception when DTO description is null") {
                    val dtoWithNull = OfferRequestDTO(title = "t", description = null, price = 1)
                    shouldThrow<NullPointerException> {
                        baseOffer.toUpdateWithRequestDto(dtoWithNull)
                    }
                }

                it("throws exception when DTO price is null") {
                    val dtoWithNull = OfferRequestDTO(title = "t", description = "d", price = null)
                    shouldThrow<NullPointerException> {
                        baseOffer.toUpdateWithRequestDto(dtoWithNull)
                    }
                }
            }
        }
    }
}
