package com.app.canyonfrs.kingmojang.memo

import com.app.canyonfrs.kingmojang.member.MemberFixture.Companion.aMember
import com.app.canyonfrs.kingmojang.member.MemberRepository
import com.app.canyonfrs.kingmojang.memo.MemoFixture.Companion.aMemo
import com.app.canyonfrs.kingmojang.memo.MemoFixture.Companion.memos
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MemoServiceTest {
    @Autowired
    lateinit var memoService: MemoService

    @Autowired
    lateinit var memoRepository: MemoRepository

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    fun `getMemos - pass | get first page order by created_date_time DESC default`() {
        // given
        val memos = mutableListOf<Memo>()
        (1..40).forEach {
            val member = memberRepository.save(aMember(id = null))
            val memo = memoRepository.save(aMemo(writerId = member.id!!, id = null))
            memos.add(memo)
        }

        // when
        // idx : 20 ~ 39(최신) 조회
        val memosResponse: MemoCursorPageResponse = memoService.getMemos(
            MemoCursorPageCondition(lastCursorId = Long.MAX_VALUE, pageSize = 20, streamerId = null)
        )

        // then
        assertThat(memosResponse.data.size).isEqualTo(20)
        assertThat(memosResponse.data[0].memoId).isEqualTo(memos[39].id)
        assertThat(memosResponse.hasNext).isTrue()
        assertThat(memosResponse.nextCursorId).isEqualTo(memos[20].id)
        assertThat(memosResponse.isEmpty).isFalse()
    }

    @Test
    fun `getMemos - pass | get second page order by created_date_time DESC default`() {
        // given
        val memos = mutableListOf<Memo>()
        (1..40).forEach {
            val member = memberRepository.save(aMember(id = null))
            val memo = memoRepository.save(aMemo(writerId = member.id!!, id = null))
            memos.add(memo)
        }

        // when
        // idx : 0 ~ 19 조회
        val memosResponse: MemoCursorPageResponse = memoService.getMemos(
            MemoCursorPageCondition(lastCursorId = memos[20].id!!, pageSize = 20, streamerId = null)
        )

        // then
        assertThat(memosResponse.data.size).isEqualTo(20)
        assertThat(memosResponse.data[0].memoId).isEqualTo(memos[19].id)
        assertThat(memosResponse.hasNext).isTrue()
        assertThat(memosResponse.nextCursorId).isEqualTo(memos[0].id)
        assertThat(memosResponse.isEmpty).isFalse()
    }

    @Test
    fun `getMemos - pass | get page of specific writer`() {
        // given
        val nonInterestedMemos = mutableListOf<Memo>()
        (1..19).forEach {
            val member = memberRepository.save(aMember(id = null))
            val memo = memoRepository.save(aMemo(writerId = member.id!!, id = null))
            nonInterestedMemos.add(memo)
        }

        val member = memberRepository.save(aMember(id = null))
        val memos = memos(size = 39, writerId = member.id!!)
        memoRepository.saveAll(memos)

        // when
        val memosResponse: MemoCursorPageResponse = memoService.getMemos(
            MemoCursorPageCondition(lastCursorId = Long.MAX_VALUE, pageSize = 20, streamerId = member.id!!)
        )

        // then
        assertThat(memosResponse.data.size).isEqualTo(20)
        assertThat(memosResponse.data[0].memoId).isEqualTo(memos[38].id)
        assertThat(memosResponse.hasNext).isTrue()
        assertThat(memosResponse.nextCursorId).isEqualTo(memos[19].id)
        assertThat(memosResponse.isEmpty).isFalse()
    }

    @Test
    fun `getMemos - pass | get second page of specific writer`() {
        // given
        val nonInterestedMemos = mutableListOf<Memo>()
        (1..19).forEach {
            val member = memberRepository.save(aMember(id = null))
            val memo = memoRepository.save(aMemo(writerId = member.id!!, id = null))
            nonInterestedMemos.add(memo)
        }

        val member = memberRepository.save(aMember(id = null))
        val memos = memos(size = 39, writerId = member.id!!)
        memoRepository.saveAll(memos)

        // when
        val memosResponse: MemoCursorPageResponse = memoService.getMemos(
            MemoCursorPageCondition(lastCursorId = memos[19].id!!, pageSize = 20, streamerId = member.id!!)
        )

        // then
        assertThat(memosResponse.data.size).isEqualTo(19)
        assertThat(memosResponse.data[0].memoId).isEqualTo(memos[18].id)
        assertThat(memosResponse.hasNext).isFalse()
        assertThat(memosResponse.nextCursorId).isEqualTo(memos[0].id)
        assertThat(memosResponse.isEmpty).isFalse()
    }

    @Test
    fun `getMemos - pass | get specific writer empty page`() {
        // given
        val member = memberRepository.save(aMember(id = null))

        // given & when
        val memosResponse: MemoCursorPageResponse = memoService.getMemos(
            MemoCursorPageCondition(lastCursorId = Long.MAX_VALUE, pageSize = 20, streamerId = member.id!!)
        )

        // then
        assertThat(memosResponse.data.size).isEqualTo(0)
        assertThat(memosResponse.hasNext).isFalse()
        assertThat(memosResponse.nextCursorId).isEqualTo(-1L)
        assertThat(memosResponse.isEmpty).isTrue()
    }
}