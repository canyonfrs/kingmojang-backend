package com.app.canyonfrs.kingmojang.memo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.restdocs.headers.HeaderDocumentation.*
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
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
                        fieldWithPath("visibility").description("생성된 메모의 공개 범위 [PUBLIC : 공개, PRIVATE : 비공개]"),
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
                        fieldWithPath("visibility").description("수정할 메모의 공개 범위 [PUBLIC : 공개, PRIVATE : 비공개]")
                    ),
                    responseFields(
                        fieldWithPath("memoId").description("수정된 메모의 ID"),
                        fieldWithPath("content").description("수정된 메모의 내용"),
                        fieldWithPath("writerId").description("수정된 메모의 작성자 ID"),
                        fieldWithPath("visibility").description("수정된 메모의 공개 범위 [PUBLIC : 공개, PRIVATE : 비공개]"),
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
                    "memos/get_by_id",
                    pathParameters(
                        parameterWithName("memoId").description("조회할 메모의 ID")
                    ),
                    responseFields(
                        fieldWithPath("memoId").description("조회된 메모의 ID"),
                        fieldWithPath("content").description("조회된 메모의 내용"),
                        fieldWithPath("writerId").description("조회된 메모의 작성자 ID"),
                        fieldWithPath("visibility").description("조회된 메모의 공개 범위 [PUBLIC : 공개, PRIVATE : 비공개]"),
                        fieldWithPath("createdDateTime").description("조회된 메모의 생성 시간"),
                        fieldWithPath("updatedDateTime").description("조회된 메모의 수정 시간")
                    )
                )
            )
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

    @Test
    fun `GET memos 200 ok document`() {
        mockMvc.perform(
            get("/api/v1/memos")
                .contentType("application/json")
                .queryParam("pageSize", "20")
                .queryParam("lastCursorId", Long.MAX_VALUE.toString())
                .queryParam("streamerId", "1")
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
            .andDo(
                document(
                    "memos/get_page",
                    queryParameters(
                        parameterWithName("pageSize").description("조회할 메모의 개수"),
                        parameterWithName("lastCursorId").description("조회할 메모리스트의 마지막 메모 ID"),
                        parameterWithName("streamerId").description("메모리스트를 조회할 스트리머 ID").optional()
                    ),
                    responseFields(
                        fieldWithPath("data").description("조회된 메모의 목록"),
                        fieldWithPath("data[].memoId").description("조회된 메모의 ID"),
                        fieldWithPath("data[].content").description("조회된 메모의 내용"),
                        fieldWithPath("data[].writerId").description("조회된 메모의 작성자 ID"),
                        fieldWithPath("data[].visibility").description("조회된 메모의 공개 범위 [PUBLIC : 공개, PRIVATE : 비공개]"),
                        fieldWithPath("data[].createdDateTime").description("조회된 메모의 생성 시간"),
                        fieldWithPath("data[].updatedDateTime").description("조회된 메모의 수정 시간"),
                        fieldWithPath("hasNext").description("다음 페이지가 있는지 여부 : 페이지 크기가 pageSize와 같으면 true"),
                        fieldWithPath("nextCursorId").description("다음 페이지의 시작 ID"),
                        fieldWithPath("isEmpty").description("조회된 메모가 없는지 여부")
                    )
                )
            )
    }
}
