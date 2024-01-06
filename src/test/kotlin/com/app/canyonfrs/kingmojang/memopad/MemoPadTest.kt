package com.app.canyonfrs.kingmojang.memopad

import com.app.canyonfrs.kingmojang.member.MemberFixture.Companion.aMember
import com.app.canyonfrs.kingmojang.memopad.MemoPadFixture.Companion.aMemoPad
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MemoPadTest {

    @Test
    fun `update-fail | writer different`() {
        val member = aMember(id = 1L)
        val memo = aMemoPad(writerId = 2L)

        Assertions.assertThatThrownBy {
            memo.update(member, "newContent", Visibility.PUBLIC)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("요청한 메모의 작성자가 아닙니다.")
    }
}