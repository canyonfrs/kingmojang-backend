package com.app.canyonfrs.kingmojang.memo

import com.app.canyonfrs.kingmojang.member.MemberFixture.Companion.aMember
import com.app.canyonfrs.kingmojang.utils.WithMockStreamerMember
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.headers.HeaderDocumentation.*
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@WebMvcTest(controllers = [MemoController::class])
@AutoConfigureRestDocs
class MemoDocumentTest {
    @Autowired
    private lateinit var mockMvc: MockMvc
    @MockkBean
    private lateinit var memoService: MemoService

    companion object {
        const val TEST_STREAMER_TOKEN = "streamer_token"
        val testStreamer = aMember(id = 1L, token = TEST_STREAMER_TOKEN)
    }

    @Test
    @WithMockStreamerMember
    fun `POST memo 201 created document`() {
        val response = MemoResponse(
            memoId = 1L,
            content = "",
            writerId = testStreamer.id!!,
            visibility = Visibility.PUBLIC,
            createdDateTime = LocalDateTime.now(),
            updatedDateTime = LocalDateTime.now()
        )

        every { memoService.createMemo(any()) } returns response

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/memos")
                .header("Authorization", "Bearer $TEST_STREAMER_TOKEN")
                .contentType("application/json")
                .with(csrf().asHeader())
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(
                MockMvcRestDocumentation.document(
                    "memos/create",
                    preprocessRequest(Preprocessors.prettyPrint()),
                    preprocessResponse(Preprocessors.prettyPrint()),
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
                    ),
                )
            )
    }

    @Test
    @WithMockStreamerMember
    fun `PUT memo 200 ok document`() {
        val request = """
            {
                "content": "수정된 메모",
                "visibility": "PUBLIC"
            }
        """.trimIndent()

        val response = MemoResponse(
            memoId = 1L,
            content = "수정된 메모",
            writerId = testStreamer.id!!,
            visibility = Visibility.PUBLIC,
            createdDateTime = LocalDateTime.now(),
            updatedDateTime = LocalDateTime.now()
        )

        every { memoService.updateMemo(any(), 1, any()) } returns response

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/v1/memos/1")
                .header("Authorization", "Bearer $TEST_STREAMER_TOKEN")
                .content(request)
                .contentType("application/json")
                .with(csrf().asHeader())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "memos/update",
                    preprocessRequest(Preprocessors.prettyPrint()),
                    preprocessResponse(Preprocessors.prettyPrint()),
                    requestHeaders(
                        headerWithName("Authorization").description("스트리머 토큰")
                    ),
                    responseFields(
                        fieldWithPath("memoId").description("수정된 메모의 ID"),
                        fieldWithPath("content").description("수정된 메모의 내용"),
                        fieldWithPath("writerId").description("수정된 메모의 작성자 ID"),
                        fieldWithPath("visibility").description("수정된 메모의 공개 범위 [PUBLIC : 공개, PRIVATE : 비공개]"),
                        fieldWithPath("createdDateTime").description("수정된 메모의 생성 시간"),
                        fieldWithPath("updatedDateTime").description("수정된 메모의 수정 시간")
                    ),
                )
            )
    }

    @Test
    @WithMockStreamerMember
    fun `GET memo 200 ok document`() {
        val response = MemoResponse(
            memoId = 1L,
            content = "생성된 메모",
            writerId = testStreamer.id!!,
            visibility = Visibility.PUBLIC,
            createdDateTime = LocalDateTime.now(),
            updatedDateTime = LocalDateTime.now()
        )

        every { memoService.getMemo(any()) } returns response

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/memos/1")
                .header("Authorization", "Bearer $TEST_STREAMER_TOKEN")
                .contentType("application/json")
                .with(csrf().asHeader())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "memos/get",
                    preprocessRequest(Preprocessors.prettyPrint()),
                    preprocessResponse(Preprocessors.prettyPrint()),
                    requestHeaders(
                        headerWithName("Authorization").description("스트리머 토큰")
                    ),
                    responseFields(
                        fieldWithPath("memoId").description("수정된 메모의 ID"),
                        fieldWithPath("content").description("수정된 메모의 내용"),
                        fieldWithPath("writerId").description("수정된 메모의 작성자 ID"),
                        fieldWithPath("visibility").description("수정된 메모의 공개 범위 [PUBLIC : 공개, PRIVATE : 비공개]"),
                        fieldWithPath("createdDateTime").description("수정된 메모의 생성 시간"),
                        fieldWithPath("updatedDateTime").description("수정된 메모의 수정 시간")
                    ),
                )
            )
    }

    @Test
    @WithMockStreamerMember
    fun `GET memos 200 ok document`() {
        val response = MemoCursorPageResponse(
            data = listOf(
                MemoResponse(
                    memoId = 1L,
                    content = "생성된 메모",
                    writerId = testStreamer.id!!,
                    visibility = Visibility.PUBLIC,
                    createdDateTime = LocalDateTime.now(),
                    updatedDateTime = LocalDateTime.now()
                )
            ),
            hasNext = false,
            nextCursorId = 1L,
            isEmpty = false
        )

        every { memoService.getMemos(any()) } returns response

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/memos")
                .contentType("application/json")
                .queryParam("pageSize", "20")
                .queryParam("lastCursorId", Long.MAX_VALUE.toString())
                .queryParam("streamerId", "2")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect {
                MockMvcResultMatchers.jsonPath("$.data").exists()
                MockMvcResultMatchers.jsonPath("$.data[0].memoId").exists()
                MockMvcResultMatchers.jsonPath("$.data[0].content").exists()
                MockMvcResultMatchers.jsonPath("$.data[0].writerId").exists()
                MockMvcResultMatchers.jsonPath("$.data[0].visibility").exists()
                MockMvcResultMatchers.jsonPath("$.data[0].createdDateTime").exists()
                MockMvcResultMatchers.jsonPath("$.data[0].updatedDateTime").exists()
                MockMvcResultMatchers.jsonPath("$.hasNext").exists()
                MockMvcResultMatchers.jsonPath("$.nextCursorId").exists()
                MockMvcResultMatchers.jsonPath("$.isEmpty").exists()
            }
            .andDo(
                MockMvcRestDocumentation.document(
                    "memos/get_page",
                    preprocessRequest(Preprocessors.prettyPrint()),
                    preprocessResponse(Preprocessors.prettyPrint()),
                    RequestDocumentation.queryParameters(
                        RequestDocumentation.parameterWithName("pageSize").description("조회할 메모의 개수"),
                        RequestDocumentation.parameterWithName("lastCursorId").description("조회할 메모리스트의 마지막 메모 ID"),
                        RequestDocumentation.parameterWithName("streamerId").optional()
                            .description("메모리스트를 조회할 스트리머 ID")
                    ),
                    responseFields(
                        fieldWithPath("data").description("조회된 메모의 목록"),
                        fieldWithPath("data[].memoId").type(JsonFieldType.NUMBER)
                            .description("조회된 메모의 ID"),
                        fieldWithPath("data[].content").type(JsonFieldType.STRING)
                            .description("조회된 메모의 내용"),
                        fieldWithPath("data[].writerId").type(JsonFieldType.NUMBER)
                            .description("조회된 메모의 작성자 ID"),
                        fieldWithPath("data[].visibility").type(JsonFieldType.STRING)
                            .description("조회된 메모의 공개 범위 [PUBLIC : 공개, PRIVATE : 비공개]"),
                        fieldWithPath("data[].createdDateTime").type(JsonFieldType.STRING)
                            .description("조회된 메모의 생성 시간"),
                        fieldWithPath("data[].updatedDateTime").type(JsonFieldType.STRING)
                            .description("조회된 메모의 수정 시간"),
                        fieldWithPath("hasNext").description("다음 페이지가 있는지 여부 : 페이지 크기가 pageSize와 같으면 true"),
                        fieldWithPath("nextCursorId").description("다음 페이지의 시작 ID"),
                        fieldWithPath("isEmpty").description("조회된 메모가 없는지 여부")
                    )
                )
            )
    }
}