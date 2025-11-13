package com.example.olx.offers.controller

import com.example.olx.BaseIntegrationTest
import com.example.olx.exceptions.dto.ErrorDTO
import com.example.olx.exceptions.dto.ErrorResponse
import com.example.olx.exceptions.dto.ErrorType
import com.example.olx.offers.dto.PostDetailsDTO
import com.example.olx.offers.dto.PostRequestDTO
import com.example.olx.offers.dto.PostSummaryDTO
import com.example.olx.offers.entity.Post
import com.example.olx.offers.repository.PostRepository
import io.kotest.matchers.date.shouldBeBefore
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.Instant

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

                response.status.shouldBe(HttpStatus.OK.value())
                with(returnedPosts[0]) {
                    id.shouldNotBeNull()
                    title.shouldBe("post_title_1")
                    description.shouldBe("post_description_1")
                    createdAt.shouldBeBefore(Instant.now())
                    updatedAt.shouldBeBefore(Instant.now())
                }
                with(returnedPosts[1]) {
                    id.shouldNotBeNull()
                    title.shouldBe("post_title_2")
                    description.shouldBe("post_description_2")
                    createdAt.shouldBeBefore(Instant.now())
                    updatedAt.shouldBeBefore(Instant.now())
                }
            }

            it("gets empty list, when there are no posts") {
                val response = mockMvc.get("/api/posts")
                    .andExpect { status { isOk() } }
                    .andReturn().response

                val returnedPosts = objectMapper.readValue(response.contentAsString, Array<PostSummaryDTO>::class.java)

                response.status.shouldBe(HttpStatus.OK.value())
                returnedPosts.count()
                    .shouldBe(0)
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
                val postToUpdate = posts[0]
                val response = mockMvc.get("/api/posts/${postToUpdate.id}")
                    .andReturn().response

                val returnedPost = objectMapper.readValue(response.contentAsString, PostDetailsDTO::class.java)

                response.status.shouldBe(HttpStatus.OK.value())
                with(returnedPost) {
                    id.shouldBe(postToUpdate.id)
                    title.shouldBe("post_title_1")
                    description.shouldBe("post_description_1")
                    createdAt.shouldBe(postToUpdate.createdAt)
                    updatedAt.shouldBe(postToUpdate.updatedAt)
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

                response.status.shouldBe(HttpStatus.NOT_FOUND.value())
                returnedError.shouldBe(expectedError)
            }
        }

        describe("POST /api/posts") {
            it("adds new post") {
                val requestedPost =
                    PostRequestDTO(title = "post_request_title_1", description = "post_request_description_1")

                val response = mockMvc.post("/api/posts") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(requestedPost)
                }
                    .andReturn().response

                val returnedPost = objectMapper.readValue(response.contentAsString, PostDetailsDTO::class.java)

                response.status.shouldBe(HttpStatus.CREATED.value())
                with(returnedPost) {
                    id.shouldNotBeNull()
                    title.shouldBe("post_request_title_1")
                    description.shouldBe("post_request_description_1")
                    createdAt.shouldBeBefore(Instant.now())
                    updatedAt.shouldBeBefore(Instant.now())
                }

                val postFromDb = postRepository.findById(returnedPost.id)
                    .get()

                with(postFromDb) {
                    id.shouldBe(returnedPost.id)
                    title.shouldBe("post_request_title_1")
                    description.shouldBe("post_request_description_1")
                    createdAt!!.shouldBeBefore(Instant.now())
                    updatedAt!!.shouldBeBefore(Instant.now())
                }
            }

            it("gets an error, when title is not set") {
                val requestedPost =
                    PostRequestDTO(title = null, description = "post_request_description_1")

                val response = mockMvc.post("/api/posts") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(requestedPost)
                }
                    .andReturn().response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                val expectedError = ErrorResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    path = "uri=/api/posts",
                    errors = listOf(
                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Title is required")
                    )
                )

                response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                returnedError.shouldBe(expectedError)
            }

            it("gets an error, when description is not set") {
                val requestedPost =
                    PostRequestDTO(title = "post_request_title_1", description = null)

                val response = mockMvc.post("/api/posts") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(requestedPost)
                }
                    .andReturn().response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                val expectedError = ErrorResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    path = "uri=/api/posts",
                    errors = listOf(
                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Description is required")
                    )
                )

                response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                returnedError.shouldBe(expectedError)
            }
        }

        describe("DELETE /api/posts/{id}") {
            it("deletes post by id") {
                val posts = postRepository.saveAll(
                    listOf(
                        Post(title = "post_title_1", description = "post_description_1"),
                        Post(title = "post_title_2", description = "post_description_2")
                    )
                )
                val removedPost = posts[0]
                val notRemovedPost = posts[1]

                val response = mockMvc.delete("/api/posts/${removedPost.id}")
                    .andReturn().response

                response.status.shouldBe(HttpStatus.NO_CONTENT.value())

                val postsFromDb = postRepository.findAll()

                postsFromDb.count()
                    .shouldBe(1)
                with(postsFromDb[0]) {
                    id.shouldBe(notRemovedPost.id)
                    title.shouldBe(notRemovedPost.title)
                    description.shouldBe(notRemovedPost.description)
                    createdAt.shouldBe(notRemovedPost.createdAt)
                    updatedAt.shouldBe(notRemovedPost.updatedAt)
                }
            }

            it("returns an error, when post is not found") {
                val postId = 1
                val response = mockMvc.delete("/api/posts/$postId")
                    .andReturn().response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)
                val expectedError = ErrorResponse(
                    status = HttpStatus.NOT_FOUND.value(),
                    path = "uri=/api/posts/$postId",
                    errors = listOf(
                        ErrorDTO(type = ErrorType.NOT_FOUND, message = "Post with $postId is not found")
                    )
                )

                response.status.shouldBe(HttpStatus.NOT_FOUND.value())
                returnedError.shouldBe(expectedError)
            }
        }

        describe("PUT /api/posts/{id}") {
            it("updates post by id") {
                val posts = postRepository.saveAll(
                    listOf(
                        Post(title = "post_title_1", description = "post_description_1"),
                        Post(title = "post_title_2", description = "post_description_2")
                    )
                )
                val postToUpdate = posts[0]
                val updatedPost = PostRequestDTO(title = "updated_title_1", description = "updated_description_1")

                val response = mockMvc.put("/api/posts/${postToUpdate.id}") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updatedPost)
                }
                    .andReturn().response

                val returnedPost = objectMapper.readValue(response.contentAsString, PostDetailsDTO::class.java)

                response.status.shouldBe(HttpStatus.OK.value())
                with(returnedPost) {
                    id.shouldBe(postToUpdate.id)
                    title.shouldBe(updatedPost.title)
                    description.shouldBe(updatedPost.description)
                    createdAt.shouldBe(postToUpdate.createdAt)
                    updatedAt.shouldNotBe(postToUpdate.updatedAt)
                        .shouldBeBefore(Instant.now())
                }

                val updatedPostFromDb = postRepository.findById(postToUpdate.id!!)
                    .get()

                with(updatedPostFromDb) {
                    id.shouldBe(postToUpdate.id)
                    title.shouldBe(updatedPost.title)
                    description.shouldBe(updatedPost.description)
                    createdAt.shouldBe(postToUpdate.createdAt)
                    updatedAt!!.shouldNotBe(postToUpdate.updatedAt)
                        .shouldBeBefore(Instant.now())
                }
            }

            it("returns an error, when post is not found") {
                val postId = 1
                val response = mockMvc.delete("/api/posts/$postId")
                    .andReturn().response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)
                val expectedError = ErrorResponse(
                    status = HttpStatus.NOT_FOUND.value(),
                    path = "uri=/api/posts/$postId",
                    errors = listOf(
                        ErrorDTO(type = ErrorType.NOT_FOUND, message = "Post with $postId is not found")
                    )
                )

                response.status.shouldBe(HttpStatus.NOT_FOUND.value())
                returnedError.shouldBe(expectedError)
            }

            it("returns an error, when title is not set") {
                val posts = postRepository.saveAll(
                    listOf(
                        Post(title = "post_title_1", description = "post_description_1"),
                        Post(title = "post_title_2", description = "post_description_2")
                    )
                )
                val postToUpdate = posts[0]
                val updatedPost = PostRequestDTO(title = null, description = "updated_description_1")

                val response = mockMvc.put("/api/posts/${postToUpdate.id}") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updatedPost)
                }
                    .andReturn().response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                val expectedError = ErrorResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    path = "uri=/api/posts/${postToUpdate.id}",
                    errors = listOf(
                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Title is required")
                    )
                )

                response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                returnedError.shouldBe(expectedError)
            }

            it("returns an error, when description is not set") {
                val posts = postRepository.saveAll(
                    listOf(
                        Post(title = "post_title_1", description = "post_description_1"),
                        Post(title = "post_title_2", description = "post_description_2")
                    )
                )
                val postToUpdate = posts[0]
                val updatedPost = PostRequestDTO(title = "updated_title_1", description = null)

                val response = mockMvc.put("/api/posts/${postToUpdate.id}") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updatedPost)
                }
                    .andReturn().response

                val returnedError = objectMapper.readValue(response.contentAsString, ErrorResponse::class.java)

                val expectedError = ErrorResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    path = "uri=/api/posts/${postToUpdate.id}",
                    errors = listOf(
                        ErrorDTO(type = ErrorType.NOT_VALID, message = "Description is required")
                    )
                )

                response.status.shouldBe(HttpStatus.BAD_REQUEST.value())
                returnedError.shouldBe(expectedError)
            }
        }
    }
}