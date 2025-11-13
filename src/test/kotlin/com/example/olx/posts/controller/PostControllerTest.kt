package com.example.olx.posts.controller

import com.example.olx.BaseIntegrationTest
import com.example.olx.exceptions.dto.ErrorDTO
import com.example.olx.exceptions.dto.ErrorResponse
import com.example.olx.exceptions.dto.ErrorType
import com.example.olx.posts.dto.PostDetailsDTO
import com.example.olx.posts.dto.PostSummaryDTO
import com.example.olx.posts.entity.Post
import com.example.olx.posts.repository.PostRepository
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.get

class PostControllerTest : BaseIntegrationTest() {

    @Autowired
    lateinit var postRepository: PostRepository

    init {
        beforeEach {
            postRepository.deleteAll()
        }

        describe("GET /api/posts") {
            it("gets all posts") {
                postRepository.saveAll(
                    listOf(
                        Post(title = "post_title_1", description = "post_description_1"),
                        Post(title = "post_title_2", description = "post_description_2")
                    )
                )

                val response = mockMvc.get("/api/posts")
                    .andReturn().response

                val returnedPosts = objectMapper.readValue(response.contentAsString, Array<PostSummaryDTO>::class.java)

                response.status shouldBe HttpStatus.OK.value()
                with(returnedPosts[0]) {
                    title shouldBe "post_title_1"
                    description shouldBe "post_description_1"
                }
                with(returnedPosts[1]) {
                    title shouldBe "post_title_2"
                    description shouldBe "post_description_2"
                }
            }

            it("gets empty list, when there are no posts") {
                val response = mockMvc.get("/api/posts")
                    .andExpect { status { isOk() } }
                    .andReturn().response

                val returnedPosts = objectMapper.readValue(response.contentAsString, Array<PostSummaryDTO>::class.java)

                response.status shouldBe HttpStatus.OK.value()
                returnedPosts.count() shouldBe 0
            }
        }

        describe("GET /api/posts/{id}") {
            it("gets post by id") {
                val posts = postRepository.saveAll(
                    listOf(
                        Post(title = "post_title_1", description = "post_description_1"),
                        Post(title = "post_title_2", description = "post_description_2")
                    )
                )
                val postId = posts[0].id
                val response = mockMvc.get("/api/posts/${postId}")
                    .andReturn().response

                val returnedPost = objectMapper.readValue(response.contentAsString, PostDetailsDTO::class.java)

                response.status shouldBe HttpStatus.OK.value()
                with(returnedPost) {
                    id shouldBe postId
                    title shouldBe "post_title_1"
                    description shouldBe "post_description_1"
                }
            }

            it("returns an error, when post is not found") {
                val postId = 1
                val response = mockMvc.get("/api/posts/$postId")
                    .andReturn().response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)
                val expectedError = ErrorResponse(
                    status = HttpStatus.NOT_FOUND.value(),
                    path = "uri=/api/posts/$postId",
                    errors = listOf(
                        ErrorDTO(type = ErrorType.NOT_FOUND, message = "Post with $postId is not found")
                    )
                )

                response.status shouldBe HttpStatus.NOT_FOUND.value()
                returnedError shouldBe expectedError
            }
        }

        describe()
    }
}