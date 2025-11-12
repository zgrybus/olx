package com.example.olx.posts.controller

import com.example.olx.BaseIntegrationTest
import com.example.olx.posts.dto.PostSummaryDTO
import com.example.olx.posts.entity.Post
import com.example.olx.posts.repository.PostRepository
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.get

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostControllerTest : BaseIntegrationTest() {

    @Autowired
    lateinit var postRepository: PostRepository

    init {
        describe("GET /api/posts") {

            beforeEach {
                postRepository.deleteAll()
            }

            it("gets all posts") {
                postRepository.saveAll(
                    listOf(
                        Post(title = "post_title_1", description = "post_description_1"),
                        Post(title = "post_title_2", description = "post_description_2")
                    )
                )

                val response = mockMvc.get("/api/posts")
                    .andExpect { status { isOk() } }
                    .andReturn().response.contentAsString

                val postsDTO = objectMapper.readValue(response, Array<PostSummaryDTO>::class.java)

                postsDTO.count() shouldBe 2
                with(postsDTO[0]) {
                    title shouldBe "post_title_1"
                    description shouldBe "post_description_1"
                }

                with(postsDTO[1]) {
                    title shouldBe "post_title_2"
                    description shouldBe "post_description_2"
                }
            }

            it("gets empty list, when there are no posts") {
                val response = mockMvc.get("/api/posts")
                    .andExpect { status { isOk() } }
                    .andReturn().response.contentAsString

                val postsDTO = objectMapper.readValue(response, Array<PostSummaryDTO>::class.java)

                postsDTO.count() shouldBe 0
            }
        }
    }
}