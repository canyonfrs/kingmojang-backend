package com.app.canyonfrs.kingmojang.member

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class MemberController(
    val memberService: MemberService
) {

    // TODO: member null일 수도 있음
    @PostMapping("/api/v1/members")
    fun createMember(
        @AuthenticationPrincipal requester: Member,
        @RequestBody memberRequest: MemberRequest
    ): ResponseEntity<MemberResponse> {
        val response = memberService.createMember(requester, memberRequest)

        return ResponseEntity.created(URI("/api/v1/members/${response.id}")).body(response)
    }
}