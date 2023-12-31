package com.app.canyonfrs.kingmojang.member

import com.app.canyonfrs.kingmojang.member.MemberFixture.Companion.aMember
import com.app.canyonfrs.kingmojang.member.MemberFixture.Companion.aMemberRequest
import com.app.canyonfrs.kingmojang.utils.AcceptanceTest
import com.app.canyonfrs.kingmojang.utils.toResponse
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AcceptanceTest
@AutoConfigureMockMvc
class MemberAcceptanceTest @Autowired constructor(
    val memberRepository: MemberRepository,
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) : BehaviorSpec({

    Given("admin") {
        val admin = memberRepository.save(aMember(role = Role.ADMIN))

        val streamerName = "new_streamer"
        val streamerEmail = "new_streamer_email@test.com"
        val streamerPhoneNumber = "111-1123-1234"
        val memberRequest = objectMapper.writeValueAsString(
            MemberRequest(
                name = streamerName,
                email = streamerEmail,
                phoneNumber = streamerPhoneNumber,
                role = Role.STREAMER
            )
        )

        When("request creating a streamer") {
            val result = mockMvc.createMember(admin.token, memberRequest)

            Then("response a streamer with 201 created") {
                val response = result.andExpect(status().isCreated)
                    .andExpect(header().exists("Location"))
                    .toResponse<MemberResponse>(objectMapper)

                response.name shouldBe streamerName
                response.email shouldBe streamerEmail
                response.phoneNumber shouldBe streamerPhoneNumber
            }
        }
    }

//    Given("non-admin") {
//        val streamer = memberRepository.save(aMember(role = Role.STREAMER))
//
//        When("request creating a streamer") {
//            val memberRequest = objectMapper.writeValueAsString(aMemberRequest())
//            val result = mockMvc.createMember(streamer.token, memberRequest)
//
//            Then("response 403 forbidden") {
//                result.andExpect(status().isForbidden)
//            }
//        }
//    }

    Given("valid verification code") {
        val admin = memberRepository.save(aMember(role = Role.ADMIN))

        val memberResponse = mockMvc.createMember(admin.token, objectMapper.writeValueAsString(aMemberRequest()))
            .andExpect(status().isCreated)
            .andExpect(header().exists("Location"))
            .toResponse<MemberResponse>(objectMapper)

        When("requested validation") {
            val result = mockMvc.validateVerificationCode(memberResponse.token)

            Then("response 200 ok") {
                val response = result.andExpect(status().isOk)
                    .toResponse<MemberResponse>(objectMapper)

                response.name shouldBe memberResponse.name
                response.email shouldBe memberResponse.email
                response.phoneNumber shouldBe memberResponse.phoneNumber
            }
        }
    }

    Given("unvalidated verification code") {
        When("requested validation") {
            val result = mockMvc.validateVerificationCode("invalid_token")

            Then("response 401 unauthorized") {
                result.andExpect(status().isUnauthorized)
            }
        }
    }
})
