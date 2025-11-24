package com.example.olx.offers.repository

import com.example.olx.offers.entity.Offer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface OfferRepository :
    JpaRepository<Offer, Long>,
    JpaSpecificationExecutor<Offer>
