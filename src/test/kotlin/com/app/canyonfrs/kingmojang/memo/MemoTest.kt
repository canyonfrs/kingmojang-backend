package com.app.canyonfrs.kingmojang.memo

import com.app.canyonfrs.kingmojang.member.MemberFixture.Companion.aMember
import com.app.canyonfrs.kingmojang.memo.MemoFixture.Companion.aMemo
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MemoTest {

    @Test
    fun `update-fail | writer different`() {
        val member = aMember(id = 1L)
        val memo = aMemo(writerId = 2L)

        Assertions.assertThatThrownBy {
            memo.update(member, "newContent", Visibility.PUBLIC)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("요청한 메모의 작성자가 아닙니다.")


    }
}