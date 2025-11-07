package com.example.olx.posts.repository

import com.example.olx.posts.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PostRepository : JpaRepository<Post, Int>, JpaSpecificationExecutor<Post> {}