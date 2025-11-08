package com.example.olx.posts.service

import com.example.olx.Loggable
import com.example.olx.posts.dto.PostDetailsDTO
import com.example.olx.posts.dto.PostRequestDTO
import com.example.olx.posts.dto.PostSummaryDTO
import com.example.olx.posts.exceptions.PostNotFoundException
import com.example.olx.posts.mapper.toDetailsDTO
import com.example.olx.posts.mapper.toEntity
import com.example.olx.posts.mapper.toSummaryDTO
import com.example.olx.posts.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    val postRepository: PostRepository,
) : Loggable {

    @Transactional(readOnly = true)
    fun getAllPosts(): List<PostSummaryDTO> {
        logger.info { "Attempting to get all posts" }

        return postRepository.findAll()
            .also {
                logger.info { "Successfully found ${it.size} posts" }
            }
            .let {
                it.map { post ->
                    post.toSummaryDTO()
                }
            }
    }

    @Transactional(readOnly = true)
    fun getPostById(postId: Int): PostDetailsDTO {
        logger.info { "Attempting to get post by id $postId" }

        return postRepository.findById(postId)
            .orElseThrow { PostNotFoundException("Post with $postId is not found") }
            .also {
                logger.info { "Successfully found post with id $postId" }
            }
            .toDetailsDTO()
    }

    @Transactional
    fun deletePostById(postId: Int) {
        logger.info { "Attempting to delete post by id $postId" }

        postRepository.findById(postId)
            .orElseThrow { PostNotFoundException("Post with $postId is not found") }
            .let {
                postRepository.delete(it)
            }
            .also {
                logger.info { "Successfully found post with id $postId" }
            }
    }

    @Transactional
    fun addPost(postRequestDTO: PostRequestDTO): PostDetailsDTO {
        logger.info { "Attempting to add post with details: $postRequestDTO" }

        return postRepository.save(postRequestDTO.toEntity())
            .also { logger.info { "Successfully added post: $it" } }
            .toDetailsDTO()
    }

    @Transactional
    fun updatePost(postId: Int, postRequestDTO: PostRequestDTO): PostDetailsDTO {
        logger.info { "Attempting to update post by id $postId" }

        return postRepository.findById(postId)
            .orElseThrow { PostNotFoundException("Post with $postId is not found") }
            .apply {
                title = postRequestDTO.title!!
                description = postRequestDTO.description!!
            }
            .toDetailsDTO()
    }
}