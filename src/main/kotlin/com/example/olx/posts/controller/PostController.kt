package com.example.olx.posts.controller

import com.example.olx.posts.entity.Post
import com.example.olx.posts.service.PostService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class PostController(val postService: PostService) {

    @GetMapping
    fun getAllPosts(): List<Post> = postService.getAllPosts()
}