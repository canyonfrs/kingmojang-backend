package com.app.canyonfrs.kingmojang.member

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    val memberRepository: MemberRepository,
    val tokenGenerator: TokenGenerator
) {

    @Transactional
    fun createMember(requester: Member, memberRequest: MemberRequest): MemberResponse {
        val newMember = requester.createMember(memberRequest, tokenGenerator)

        memberRepository.save(newMember)

        return MemberResponse.of(newMember)
    }
}