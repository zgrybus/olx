package com.example.olx.posts.controller

import com.example.olx.posts.dto.PostSummaryDTO
import com.example.olx.posts.entity.Post
import com.example.olx.posts.repository.PostRepository
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PostControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val postRepository: PostRepository
) : BehaviorSpec({

    extension(SpringExtension)

    beforeEach {
        postRepository.deleteAll()
    }

    Given("GET /api/posts") {

        When("no posts exists") {
            val result = mockMvc.get("/api/posts")
                .andExpect { status { isOk() } }
                .andReturn()

            Then("posts list is empty") {
                val posts = objectMapper.readValue(result.response.contentAsString, Array<PostSummaryDTO>::class.java)
                posts.count() shouldBe 0
            }
        }

        When("posts exists") {
            postRepository.saveAll(
                listOf(
                    Post(title = "title_1", description = "description_1"),
                    Post(title = "title_2", description = "description_2")
                )
            )

            val result = mockMvc.get("/api/posts")
                .andExpect { status { isOk() } }
                .andReturn()

            Then("posts list contains all of them") {
                val posts = objectMapper.readValue(result.response.contentAsString, Array<PostSummaryDTO>::class.java)

                posts.count() shouldBe 2

                with(posts[0]) {
                    title shouldBe "title_1"
                    description shouldBe "description_1"
                }
                with(posts[1]) {
                    title shouldBe "title_2"
                    description shouldBe "description_2"
                }
            }
        }
    }
})