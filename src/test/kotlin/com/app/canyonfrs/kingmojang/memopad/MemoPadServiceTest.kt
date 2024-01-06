package com.app.canyonfrs.kingmojang.memopad

import com.app.canyonfrs.kingmojang.member.MemberFixture.Companion.aMember
import com.app.canyonfrs.kingmojang.member.MemberRepository
import com.app.canyonfrs.kingmojang.memopad.MemoPadFixture.Companion.aMemoPad
import com.app.canyonfrs.kingmojang.memopad.MemoPadFixture.Companion.memoPads
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MemoPadServiceTest {
    @Autowired
    lateinit var memoPadService: MemoPadService

    @Autowired
    lateinit var memoPadRepository: MemoPadRepository

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    fun `getMemos - pass | get first page order by created_date_time DESC default`() {
        // given
        val memoPads = mutableListOf<MemoPad>()
        (1..40).forEach {
            val member = memberRepository.save(aMember(id = null))
            val memo = memoPadRepository.save(aMemoPad(writerId = member.id!!, id = null))
            memoPads.add(memo)
        }

        // when
        // idx : 20 ~ 39(최신) 조회
        val memosResponse: MemoPadCursorPageResponse = memoPadService.getMemoPads(
            MemoPadCursorPageCondition(lastCursorId = Long.MAX_VALUE, pageSize = 20, streamerId = null)
        )

        // then
        assertThat(memosResponse.data.size).isEqualTo(20)
        assertThat(memosResponse.data[0].memoPadId).isEqualTo(memoPads[39].id)
        assertThat(memosResponse.hasNext).isTrue()
        assertThat(memosResponse.nextCursorId).isEqualTo(memoPads[20].id)
        assertThat(memosResponse.isEmpty).isFalse()
    }

    @Test
    fun `getMemos - pass | get second page order by created_date_time DESC default`() {
        // given
        val memoPads = mutableListOf<MemoPad>()
        (1..40).forEach {
            val member = memberRepository.save(aMember(id = null))
            val memo = memoPadRepository.save(aMemoPad(writerId = member.id!!, id = null))
            memoPads.add(memo)
        }

        // when
        // idx : 0 ~ 19 조회
        val memosResponse: MemoPadCursorPageResponse = memoPadService.getMemoPads(
            MemoPadCursorPageCondition(lastCursorId = memoPads[20].id!!, pageSize = 20, streamerId = null)
        )

        // then
        assertThat(memosResponse.data.size).isEqualTo(20)
        assertThat(memosResponse.data[0].memoPadId).isEqualTo(memoPads[19].id)
        assertThat(memosResponse.hasNext).isTrue()
        assertThat(memosResponse.nextCursorId).isEqualTo(memoPads[0].id)
        assertThat(memosResponse.isEmpty).isFalse()
    }

    @Test
    fun `getMemos - pass | get page of specific writer`() {
        // given
        val nonInterestedMemoPads = mutableListOf<MemoPad>()
        (1..19).forEach {
            val member = memberRepository.save(aMember(id = null))
            val memo = memoPadRepository.save(aMemoPad(writerId = member.id!!, id = null))
            nonInterestedMemoPads.add(memo)
        }

        val member = memberRepository.save(aMember(id = null))
        val memos = memoPads(size = 39, writerId = member.id!!)
        memoPadRepository.saveAll(memos)

        // when
        val memosResponse: MemoPadCursorPageResponse = memoPadService.getMemoPads(
            MemoPadCursorPageCondition(lastCursorId = Long.MAX_VALUE, pageSize = 20, streamerId = member.id!!)
        )

        // then
        assertThat(memosResponse.data.size).isEqualTo(20)
        assertThat(memosResponse.data[0].memoPadId).isEqualTo(memos[38].id)
        assertThat(memosResponse.hasNext).isTrue()
        assertThat(memosResponse.nextCursorId).isEqualTo(memos[19].id)
        assertThat(memosResponse.isEmpty).isFalse()
    }

    @Test
    fun `getMemos - pass | get second page of specific writer`() {
        // given
        val nonInterestedMemoPads = mutableListOf<MemoPad>()
        (1..19).forEach {
            val member = memberRepository.save(aMember(id = null))
            val memo = memoPadRepository.save(aMemoPad(writerId = member.id!!, id = null))
            nonInterestedMemoPads.add(memo)
        }

        val member = memberRepository.save(aMember(id = null))
        val memos = memoPads(size = 39, writerId = member.id!!)
        memoPadRepository.saveAll(memos)

        // when
        val memosResponse: MemoPadCursorPageResponse = memoPadService.getMemoPads(
            MemoPadCursorPageCondition(lastCursorId = memos[19].id!!, pageSize = 20, streamerId = member.id!!)
        )

        // then
        assertThat(memosResponse.data.size).isEqualTo(19)
        assertThat(memosResponse.data[0].memoPadId).isEqualTo(memos[18].id)
        assertThat(memosResponse.hasNext).isFalse()
        assertThat(memosResponse.nextCursorId).isEqualTo(memos[0].id)
        assertThat(memosResponse.isEmpty).isFalse()
    }

    @Test
    fun `getMemos - pass | get specific writer empty page`() {
        // given
        val member = memberRepository.save(aMember(id = null))

        // given & when
        val memosResponse: MemoPadCursorPageResponse = memoPadService.getMemoPads(
            MemoPadCursorPageCondition(lastCursorId = Long.MAX_VALUE, pageSize = 20, streamerId = member.id!!)
        )

        // then
        assertThat(memosResponse.data.size).isEqualTo(0)
        assertThat(memosResponse.hasNext).isFalse()
        assertThat(memosResponse.nextCursorId).isEqualTo(-1L)
        assertThat(memosResponse.isEmpty).isTrue()
    }
}