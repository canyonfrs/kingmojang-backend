package com.app.canyonfrs.kingmojang.member

import com.app.canyonfrs.kingmojang.utils.MemberConstant.TEST_STREAMER_TOKEN
import com.app.canyonfrs.kingmojang.utils.WithMockAdminMember
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.log
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [MemberController::class])
@AutoConfigureRestDocs
class MemberDocumentTest {
    @Autowired
    private lateinit var mockMvc: MockMvc
    @MockkBean
    private lateinit var memberService: MemberService

    @Test
    @WithMockAdminMember
    fun `POST member 201 created document`() {
        val request = MemberRequest(
            name = "test",
            email = "testStreamer@test.com",
            phoneNumber = "010-1234-5678",
            role = Role.STREAMER
        )

        val response = MemberResponse(
            id = 1,
            name = request.name,
            email = request.email,
            token = TEST_STREAMER_TOKEN.name,
            phoneNumber = request.phoneNumber,
            role = request.role
        )

        every { memberService.createMember(any(), any()) } returns response

        mockMvc.perform(
            post("/api/v1/members")
                .header("Authorization", "Bearer ${TEST_STREAMER_TOKEN.name}")
                .contentType("application/json")
                .content(jacksonObjectMapper().writeValueAsString(request))
                .with(csrf().asHeader())
        )
            .andExpect(status().isCreated)
            .andDo(log())
            .andDo(
                document(
                    "member-create",
                    requestFields(
                        fieldWithPath("name").description("생성할 회원의 이름 / 닉네임"),
                        fieldWithPath("email").description("생성할 회원의 이메일"),
                        fieldWithPath("phoneNumber").description("생성할 회원의 전화번호 (nullable)"),
                        fieldWithPath("role").description("생성할 회원의 권한 [ADMIN : 관리자, STREAMER : 스트리머, VIEWER : 시청자]")
                    ),
                    responseFields(
                        fieldWithPath("id").description("생성한 회원의 고유 id"),
                        fieldWithPath("name").description("생성한 회원의 이름 / 닉네임"),
                        fieldWithPath("email").description("생성한 회원의 이메일"),
                        fieldWithPath("token").description("생성한 회원의 자동 생성된 토큰(쿠폰)"),
                        fieldWithPath("phoneNumber").description("생성한 회원의 전화번호 (nullable)"),
                        fieldWithPath("role").description("생성한 회원의 권한 [ADMIN : 관리자, STREAMER : 스트리머, VIEWER : 시청자]")
                    )
                )
            )
    }
}