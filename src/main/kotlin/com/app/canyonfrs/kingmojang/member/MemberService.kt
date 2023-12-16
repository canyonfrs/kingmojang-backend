package com.app.canyonfrs.kingmojang.member

import com.app.canyonfrs.kingmojang.common.ActiveStatus
import com.app.canyonfrs.kingmojang.common.BusinessException
import com.app.canyonfrs.kingmojang.common.ErrorCode.INVALID_MEMBER_STATUS
import com.app.canyonfrs.kingmojang.common.ErrorCode.INVALID_VERIFICATION_CODE
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

    fun validateVerificationCode(token: String) {

        val member = memberRepository.findByToken(token) ?: throw BusinessException(INVALID_VERIFICATION_CODE)

        if (member.activeStatus != ActiveStatus.ACTIVE) {
            throw BusinessException(INVALID_MEMBER_STATUS)
        }
    }
}
