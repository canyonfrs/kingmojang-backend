package com.app.canyonfrs.kingmojang.member

import com.app.canyonfrs.kingmojang.member.MemberFixture.Companion.aMember
import com.app.canyonfrs.kingmojang.member.MemberFixture.Companion.aMemberRequest
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource

class MemberTest {
    @Test
    fun `createEmptyMemo - fail = not streamer`() {
        val nonStreamers = listOf(Role.ADMIN, Role.VIEWER)

        nonStreamers.forEach { nonStreamer ->
            val member = aMember(role = nonStreamer)
            assertThatThrownBy { member.createEmptyMemo() }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("메모 생성은 스트리머만 가능합니다.")
        }
    }

    @Test
    fun `createMember - fail = not admin`() {
        val nonAdmins = listOf(Role.STREAMER, Role.VIEWER)

        nonAdmins.forEach { nonAdmin ->
            val member = aMember(role = nonAdmin)
            assertThatThrownBy { member.createMember(aMemberRequest()) { "token" } }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("회원 생성은 관리자만 가능합니다.")
        }
    }

    @Test
    fun `createMember - pass`() {
        val admin = aMember(role = Role.ADMIN)
        val request = aMemberRequest()

        val member = admin.createMember(request) { "token" }

        assertThat(request.name).isEqualTo(member.name)
        assertThat(request.email).isEqualTo(member.email)
        assertThat(request.phoneNumber).isEqualTo(member.phoneNumber)
        assertThat(request.role).isEqualTo(member.role)
    }

    @ParameterizedTest
    @ValueSource(strings = ["kskyung0624@gmail.com", "wer@sdf.com", "wer@hanmail.net"])
    fun `createMember - pass = email validation`(email: String) {
        val admin = aMember(role = Role.ADMIN)
        val request = aMemberRequest(email = email)

        val member = admin.createMember(request) { "token" }

        assertThat(request.name).isEqualTo(member.name)
        assertThat(request.email).isEqualTo(member.email)
        assertThat(request.phoneNumber).isEqualTo(member.phoneNumber)
        assertThat(request.role).isEqualTo(member.role)
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = ["010-0000-0000", ""])
    fun `createMember - pass = phoneNumber validation`(phoneNumber: String?) {
        val admin = aMember(role = Role.ADMIN)
        val request = aMemberRequest(phoneNumber = phoneNumber)

        val member = admin.createMember(request) { "token" }

        assertThat(request.name).isEqualTo(member.name)
        assertThat(request.email).isEqualTo(member.email)
        assertThat(request.phoneNumber).isEqualTo(member.phoneNumber)
        assertThat(request.role).isEqualTo(member.role)
    }


    @ParameterizedTest
    @ValueSource(strings = ["tester@test", "", "tester", "tester@"])
    fun `createMember - fail = email pattern wrong`(email: String) {
        val admin = aMember(role = Role.ADMIN)
        val request = aMemberRequest(email = email)

        assertThatThrownBy { admin.createMember(request) { "token" } }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("이메일 형식이 올바르지 않습니다.")
    }

    @ParameterizedTest
    @ValueSource(strings = ["--", "0000-00-0000", "000-0000-000a", "000-0-0000"])
    fun `createMember - fail = phoneNumber pattern wrong`(phoneNumber: String) {
        val admin = aMember(role = Role.ADMIN)
        val request = aMemberRequest(phoneNumber = phoneNumber)

        assertThatThrownBy { admin.createMember(request) { "token" } }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("전화번호 형식이 올바르지 않습니다.")
    }
}
