package com.example.olx.offers.mapper

import com.example.olx.offers.dto.OfferRequestDTO
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe


class OfferRequestDTOMappers : DescribeSpec() {
    init {
        describe("toEntity") {
            it("maps from dto to entity") {
                val dto = OfferRequestDTO(title = "dto_title", description = "dto_description", price = 10)

                with(dto.toEntity()) {
                    title.shouldBe("dto_title")
                    description.shouldBe("dto_description")
                    price.shouldBe(10)
                }
            }

            describe("Exception") {
                it("throws exception when title is null") {
                    val dto = OfferRequestDTO(title = null, description = "dto_description", price = 10)

                    shouldThrow<NullPointerException> {
                        dto.toEntity()
                    }
                }

                it("throws exception when description is null") {
                    val dto = OfferRequestDTO(title = "dto_title", description = null, price = 10)

                    shouldThrow<NullPointerException> {
                        dto.toEntity()
                    }
                }

                it("throws exception when price is null") {
                    val dto = OfferRequestDTO(title = "dto_title", description = "dto_description", price = null)

                    shouldThrow<NullPointerException> {
                        dto.toEntity()
                    }
                }
            }
        }
    }
}