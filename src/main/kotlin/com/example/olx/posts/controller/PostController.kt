package com.example.olx.posts.controller

import com.example.olx.posts.dto.PostDetailsDTO
import com.example.olx.posts.dto.PostRequestDTO
import com.example.olx.posts.dto.PostSummaryDTO
import com.example.olx.posts.service.PostService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
@Validated
class PostController(val postService: PostService) {

    @GetMapping
    fun getAllPosts(): List<PostSummaryDTO> = postService.getAllPosts()

    @GetMapping("/{postId}")
    fun getPostById(@PathVariable postId: Long): PostDetailsDTO = postService.getPostById(postId)

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePostById(@PathVariable postId: Long) = postService.deletePostById(postId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addPost(@RequestBody @Valid postRequestDTO: PostRequestDTO): PostDetailsDTO =
        postService.addPost(postRequestDTO)

    @PutMapping("/{postId}")
    fun updatePost(@PathVariable postId: Long, @RequestBody @Valid postRequestDTO: PostRequestDTO): PostDetailsDTO =
        postService.updatePost(postId, postRequestDTO)
}