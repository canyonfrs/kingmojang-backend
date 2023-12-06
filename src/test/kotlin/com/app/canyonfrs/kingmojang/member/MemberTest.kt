package com.app.canyonfrs.kingmojang.member

import com.app.canyonfrs.kingmojang.member.MemberFixture.Companion.aMember
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class MemberTest {

    @Test
    fun `createEmptyMemo - fail | not streamer`() {
        val nonStreamers = listOf(Role.ADMIN, Role.VIEWER)

        nonStreamers.forEach { nonStreamer ->
            val member = aMember(role = nonStreamer)
            assertThatThrownBy { member.createEmptyMemo() }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("메모 생성은 스트리머만 가능합니다.")
        }
    }
}
