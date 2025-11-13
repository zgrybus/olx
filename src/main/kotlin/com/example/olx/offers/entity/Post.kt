package com.example.olx.offers.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
@Table(name = "posts")
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var title: String,

    @Column(columnDefinition = "TEXT")
    var description: String,

    @CreationTimestamp
    var createdAt: Instant? = null,

    @UpdateTimestamp
    var updatedAt: Instant? = null,
) {}