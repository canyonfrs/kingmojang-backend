package com.app.canyonfrs.kingmojang.utils

import com.app.canyonfrs.kingmojang.member.MemberFixture.Companion.aMember
import com.app.canyonfrs.kingmojang.member.Role
import com.app.canyonfrs.kingmojang.memo.MemoDocumentTest.Companion.TEST_STREAMER_TOKEN
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockCustomStreamerSecurityContextFactory : WithSecurityContextFactory<WithMockCustomStreamer> {
    override fun createSecurityContext(withMockCUstomStreamer: WithMockCustomStreamer?): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()
        val grantedAuthorities = mutableListOf<GrantedAuthority>()
        grantedAuthorities.add(SimpleGrantedAuthority(Role.STREAMER.name))
        val member = aMember(id = 1L, name = "test_streamer", token = TEST_STREAMER_TOKEN, role = Role.STREAMER)
        val authentication = UsernamePasswordAuthenticationToken(
            member,
            null,
            member.authorities
        )
        context.authentication = authentication

        return context
    }
}
