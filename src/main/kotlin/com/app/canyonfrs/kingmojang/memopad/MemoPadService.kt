package com.app.canyonfrs.kingmojang.memopad

import com.app.canyonfrs.kingmojang.member.Member
import com.app.canyonfrs.kingmojang.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException

interface MemoPadService {
    fun createMemo(member: Member): MemoPadResponse
    fun updateMemoPad(member: Member, memoId: Long, request: MemoPadRequest): MemoPadResponse
    fun getMemoPad(memoId: Long): MemoPadResponse
    fun getMemoPads(memoPadCursorPageCondition: MemoPadCursorPageCondition): MemoPadCursorPageResponse
}

@Service
@Transactional(readOnly = true)
class MemoPadServiceImpl(
    val memoPadRepository: MemoPadRepository,
    val memberRepository: MemberRepository
) : MemoPadService {

    @Transactional
    override fun createMemo(member: Member): MemoPadResponse {
        val memo = member.createEmptyMemo()

        return MemoPadResponse.from(memoPadRepository.save(memo))
    }

    @Transactional
    override fun updateMemoPad(member: Member, memoId: Long, request: MemoPadRequest): MemoPadResponse {
        val memo = memoPadRepository.findById(memoId)
            .orElseThrow { throw IllegalArgumentException("요청한 메모가 존재하지 않습니다.") }

        memo.update(member, request.content, request.visibility)

        return MemoPadResponse.from(memo)
    }

    override fun getMemoPad(memoId: Long): MemoPadResponse {
        val memo = memoPadRepository.findById(memoId)
            .orElseThrow { throw IllegalArgumentException("요청한 메모가 존재하지 않습니다.") }

        return MemoPadResponse.from(memo)
    }

    @Transactional
    override fun getMemoPads(memoPadCursorPageCondition: MemoPadCursorPageCondition): MemoPadCursorPageResponse {
        if (memoPadCursorPageCondition.streamerId != null) {
            memberRepository.findById(memoPadCursorPageCondition.streamerId)
                .orElseThrow { throw IllegalArgumentException("요청한 스트리머가 존재하지 않습니다.") }
        }

        val memos = memoPadRepository.findByMemoPadCursorPageCondition(memoPadCursorPageCondition)
        return MemoPadCursorPageResponse.of(memos, memoPadCursorPageCondition.pageSize)
    }
}