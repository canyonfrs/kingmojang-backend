package com.app.canyonfrs.kingmojang.memo

import com.app.canyonfrs.kingmojang.member.Member
import com.app.canyonfrs.kingmojang.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException

interface MemoService {
    fun createMemo(member: Member): MemoResponse
    fun updateMemo(member: Member, memoId: Long, request: MemoRequest): MemoResponse
    fun getMemo(memoId: Long): MemoResponse
    fun getMemos(memoCursorPageCondition: MemoCursorPageCondition): MemoCursorPageResponse
}

@Service
@Transactional(readOnly = true)
class MemoServiceImpl(
    val memoRepository: MemoRepository,
    val memberRepository: MemberRepository
) : MemoService {

    @Transactional
    override fun createMemo(member: Member): MemoResponse {
        val memo = member.createEmptyMemo()

        return MemoResponse.from(memoRepository.save(memo))
    }

    @Transactional
    override fun updateMemo(member: Member, memoId: Long, request: MemoRequest): MemoResponse {
        val memo = memoRepository.findById(memoId)
            .orElseThrow { throw IllegalArgumentException("요청한 메모가 존재하지 않습니다.") }
        memo.update(member, request.content, request.visibility)

        return MemoResponse.from(memo)
    }

    override fun getMemo(memoId: Long): MemoResponse {
        val memo = memoRepository.findById(memoId)
            .orElseThrow { throw IllegalArgumentException("요청한 메모가 존재하지 않습니다.") }

        return MemoResponse.from(memo)
    }

    @Transactional
    override fun getMemos(memoCursorPageCondition: MemoCursorPageCondition): MemoCursorPageResponse {
        if (memoCursorPageCondition.streamerId != null) {
            memberRepository.findById(memoCursorPageCondition.streamerId)
                .orElseThrow { throw IllegalArgumentException("요청한 스트리머가 존재하지 않습니다.") }
        }

        val memos = memoRepository.findByMemoCursorPageCondition(memoCursorPageCondition)
        return MemoCursorPageResponse.of(memos, memoCursorPageCondition.pageSize)
    }
}