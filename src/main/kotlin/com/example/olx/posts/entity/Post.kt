package com.example.olx.posts.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
@Table(name = "posts")
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @get:NotBlank(message = "Title is required")
    var title: String,

    @Column(columnDefinition = "TEXT")
    @get:NotBlank(message = "Description is required")
    var description: String,

    @CreationTimestamp
    var createdAt: Instant? = null,

    @UpdateTimestamp
    var updatedAt: Instant? = null,
) {}