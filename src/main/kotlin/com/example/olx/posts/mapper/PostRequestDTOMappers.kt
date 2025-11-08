package com.example.olx.posts.mapper

import com.example.olx.posts.dto.PostRequestDTO
import com.example.olx.posts.entity.Post

fun PostRequestDTO.toEntity(): Post = Post(
    id = null,
    title = this.title,
    description = this.description,
    createdAt = null,
    updatedAt = null,
)