package com.example.olx.posts.mapper

import com.example.olx.posts.dto.PostDetailsDTO
import com.example.olx.posts.dto.PostSummaryDTO
import com.example.olx.posts.entity.Post


fun Post.toSummaryDTO(): PostSummaryDTO = PostSummaryDTO(
    id = this.id!!,
    title = this.title,
    description = this.description,
    createdAt = this.createdAt!!,
    updatedAt = this.updatedAt!!
)

fun Post.toDetailsDTO(): PostDetailsDTO = PostDetailsDTO(
    id = this.id!!,
    title = this.title,
    description = this.description,
    createdAt = this.createdAt!!,
    updatedAt = this.updatedAt!!
)