package com.example.olx.posts.service

import com.example.olx.Loggable
import com.example.olx.posts.dto.PostDTO
import com.example.olx.posts.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class PostService(val postRepository: PostRepository) : Loggable {
    fun getAllPosts(): List<PostDTO> {
        logger.info { "Attempting to get all posts" }

        return postRepository.findAll().also {
            logger.info { "Successfully found ${it.size} posts" }
        }.let {
            it.map { post ->
                PostDTO(
                    id = post.id!!,
                    title = post.title,
                    description = post.description,
                    createdAt = post.createdAt!!,
                    updatedAt = post.updatedAt!!
                )
            }
        }
    }
}