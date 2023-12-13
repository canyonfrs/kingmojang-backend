package com.app.canyonfrs.kingmojang.memo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest
@AutoConfigureMockMvc
class MemoAcceptanceTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `POST memo 201 created`() {
        mockMvc.perform(
            post("/api/v1/memos")
                .header("Authorization", "Bearer streamer_token")
                .contentType("application/json")
        )
            .andExpect(status().isCreated)
            .andExpect(header().exists("Location"))
    }

    @Test
    fun `PUT memo 200 ok`() {
        mockMvc.perform(
            put("/api/v1/memos/{memoId}", 1)
                .content(
                    """
                    {
                        "content": "메모 내용",
                        "visibility": "PUBLIC"
                    }
                    """.trimIndent()
                )
                .header("Authorization", "Bearer streamer_token")
                .contentType("application/json")
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `GET memo 200 ok`() {
        mockMvc.perform(
            get("/api/v1/memos/{memoId}", 1)
                .contentType("application/json")
        )
            .andExpect(status().isOk)
            .andExpect {
                jsonPath("$.memoId").exists()
                jsonPath("$.content").exists()
                jsonPath("$.writerId").exists()
                jsonPath("$.visibility").exists()
                jsonPath("$.createdDateTime").exists()
                jsonPath("$.updatedDateTime").exists()
            }
    }

    @Test
    fun `GET memos 200 ok`() {
        mockMvc.perform(
            get("/api/v1/memos")
                .contentType("application/json")
                .queryParam("pageSize", "10")
                .queryParam("lastCursorId", "1")
        )
            .andExpect(status().isOk)
            .andExpect {
                jsonPath("$.data").exists()
                jsonPath("$.data[0].memoId").exists()
                jsonPath("$.data[0].content").exists()
                jsonPath("$.data[0].writerId").exists()
                jsonPath("$.data[0].visibility").exists()
                jsonPath("$.data[0].createdDateTime").exists()
                jsonPath("$.data[0].updatedDateTime").exists()
                jsonPath("$.hasNext").exists()
                jsonPath("$.nextCursorId").exists()
                jsonPath("$.isEmpty").exists()
            }
    }
}
