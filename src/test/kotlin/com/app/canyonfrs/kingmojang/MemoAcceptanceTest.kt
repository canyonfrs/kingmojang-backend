package com.app.canyonfrs.kingmojang

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.restdocs.headers.HeaderDocumentation.*
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@AutoConfigureRestDocs
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
    fun `POST memo 201 created document`() {
        mockMvc.perform(
            post("/api/v1/memos")
                .header("Authorization", "Bearer streamer_token")
                .contentType("application/json")
        )
            .andExpect(status().isCreated)
            .andDo(
                document(
                    "memos/create",
                    requestHeaders(
                        headerWithName("Authorization").description("스트리머 토큰")
                    ),
                    responseHeaders(
                        headerWithName("Location").description("생성된 메모의 URI")
                    ),
                    responseFields(
                        fieldWithPath("memoId").description("생성된 메모의 ID"),
                        fieldWithPath("content").description("생성된 메모의 내용"),
                        fieldWithPath("writerId").description("생성된 메모의 작성자 ID"),
                        fieldWithPath("visibility").description("생성된 메모의 공개 범위"),
                        fieldWithPath("createdDateTime").description("생성된 메모의 생성 시간"),
                        fieldWithPath("updatedDateTime").description("생성된 메모의 수정 시간")
                    )
                )
            )
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
    fun `PUT memo 200 ok document`() {
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
            .andDo(
                document(
                    "memos/update",
                    requestHeaders(
                        headerWithName("Authorization").description("스트리머 토큰")
                    ),
                    requestFields(
                        fieldWithPath("content").description("수정할 메모의 내용"),
                        fieldWithPath("visibility").description("수정할 메모의 공개 범위")
                    ),
                    responseFields(
                        fieldWithPath("memoId").description("수정된 메모의 ID"),
                        fieldWithPath("content").description("수정된 메모의 내용"),
                        fieldWithPath("writerId").description("수정된 메모의 작성자 ID"),
                        fieldWithPath("visibility").description("수정된 메모의 공개 범위"),
                        fieldWithPath("createdDateTime").description("수정된 메모의 생성 시간"),
                        fieldWithPath("updatedDateTime").description("수정된 메모의 수정 시간")
                    )
                )
            )
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
    fun `GET memo 200 ok document`() {
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
            .andDo(
                document(
                    "memos/get by id",
                    pathParameters(
                        parameterWithName("memoId").description("조회할 메모의 ID")
                    ),
                    responseFields(
                        fieldWithPath("memoId").description("조회된 메모의 ID"),
                        fieldWithPath("content").description("조회된 메모의 내용"),
                        fieldWithPath("writerId").description("조회된 메모의 작성자 ID"),
                        fieldWithPath("visibility").description("조회된 메모의 공개 범위"),
                        fieldWithPath("createdDateTime").description("조회된 메모의 생성 시간"),
                        fieldWithPath("updatedDateTime").description("조회된 메모의 수정 시간")
                    )
                )
            )
    }
}
