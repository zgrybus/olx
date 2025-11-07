package com.example.olx.posts.dto

import java.time.Instant

data class PostDTO(
    val id: Long,
    val title: String,
    val description: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
