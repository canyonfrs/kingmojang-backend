package com.app.canyonfrs.kingmojang.memopad

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
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@WebMvcTest(controllers = [MemoPadController::class])
@AutoConfigureRestDocs
class MemoPadDocumentTest {
    @Autowired
    private lateinit var mockMvc: MockMvc
    @MockkBean
    private lateinit var memoPadService: MemoPadService

    companion object {
        const val TEST_STREAMER_TOKEN = "streamer_token"
        val testStreamer = aMember(id = 1L, token = TEST_STREAMER_TOKEN)
    }

    @Test
    @WithMockStreamerMember
    fun `POST memo-pad 201 created document`() {
        val response = MemoPadResponse(
            memoPadId = 1L,
            content = "",
            writerId = testStreamer.id!!,
            visibility = Visibility.PUBLIC,
            createdDateTime = LocalDateTime.now(),
            updatedDateTime = LocalDateTime.now()
        )

        every { memoPadService.createMemo(any()) } returns response

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/memo-pads")
                .header("Authorization", "Bearer $TEST_STREAMER_TOKEN")
                .contentType("application/json")
                .with(csrf().asHeader())
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(
                MockMvcRestDocumentation.document(
                    "memo-pads/create",
                    preprocessRequest(Preprocessors.prettyPrint()),
                    preprocessResponse(Preprocessors.prettyPrint()),
                    requestHeaders(
                        headerWithName("Authorization").description("스트리머 토큰")
                    ),
                    responseHeaders(
                        headerWithName("Location").description("생성된 메모의 URI")
                    ),
                    responseFields(
                        fieldWithPath("memoPadId").description("생성된 메모의 ID"),
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
    fun `PUT memo-pad 200 ok document`() {
        val request = """
            {
                "content": "수정된 메모",
                "visibility": "PUBLIC"
            }
        """.trimIndent()

        val response = MemoPadResponse(
            memoPadId = 1L,
            content = "수정된 메모",
            writerId = testStreamer.id!!,
            visibility = Visibility.PUBLIC,
            createdDateTime = LocalDateTime.now(),
            updatedDateTime = LocalDateTime.now()
        )

        every { memoPadService.updateMemoPad(any(), 1, any()) } returns response

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/v1/memo-pads/1")
                .header("Authorization", "Bearer $TEST_STREAMER_TOKEN")
                .content(request)
                .contentType("application/json")
                .with(csrf().asHeader())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "memo-pads/update",
                    preprocessRequest(Preprocessors.prettyPrint()),
                    preprocessResponse(Preprocessors.prettyPrint()),
                    requestHeaders(
                        headerWithName("Authorization").description("스트리머 토큰")
                    ),
                    requestFields(
                        fieldWithPath("content").description("수정할 메모의 내용"),
                        fieldWithPath("visibility").description("수정할 메모의 공개 범위 [PUBLIC : 공개, PRIVATE : 비공개]")
                    ),
                    responseFields(
                        fieldWithPath("memoPadId").description("수정된 메모의 ID"),
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
    fun `GET memo-pad 200 ok document`() {
        val response = MemoPadResponse(
            memoPadId = 1L,
            content = "생성된 메모",
            writerId = testStreamer.id!!,
            visibility = Visibility.PUBLIC,
            createdDateTime = LocalDateTime.now(),
            updatedDateTime = LocalDateTime.now()
        )

        every { memoPadService.getMemoPad(any()) } returns response

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/memo-pads/1")
                .header("Authorization", "Bearer $TEST_STREAMER_TOKEN")
                .contentType("application/json")
                .with(csrf().asHeader())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "memo-pads/get",
                    preprocessRequest(Preprocessors.prettyPrint()),
                    preprocessResponse(Preprocessors.prettyPrint()),
                    requestHeaders(
                        headerWithName("Authorization").description("스트리머 토큰")
                    ),
                    responseFields(
                        fieldWithPath("memoPadId").description("수정된 메모의 ID"),
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
    fun `GET memo-pads 200 ok document`() {
        val response = MemoPadCursorPageResponse(
            data = listOf(
                MemoPadResponse(
                    memoPadId = 1L,
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

        every { memoPadService.getMemoPads(any()) } returns response

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/memo-pads")
                .contentType("application/json")
                .queryParam("pageSize", "20")
                .queryParam("lastCursorId", Long.MAX_VALUE.toString())
                .queryParam("streamerId", "2")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect {
                MockMvcResultMatchers.jsonPath("$.data").exists()
                MockMvcResultMatchers.jsonPath("$.data[0].memoPadId").exists()
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
                    "memo-pads/get_page",
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
                        fieldWithPath("data[].memoPadId").type(JsonFieldType.NUMBER)
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