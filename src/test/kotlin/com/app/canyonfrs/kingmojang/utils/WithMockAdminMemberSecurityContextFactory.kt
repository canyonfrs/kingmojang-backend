package com.app.canyonfrs.kingmojang.utils

import com.app.canyonfrs.kingmojang.member.MemberFixture
import com.app.canyonfrs.kingmojang.member.Role
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockAdminMemberSecurityContextFactory : WithSecurityContextFactory<WithMockAdminMember> {

    override fun createSecurityContext(withMockAdminMember: WithMockAdminMember?): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()
        val grantedAuthorities = mutableListOf<GrantedAuthority>()
        grantedAuthorities.add(SimpleGrantedAuthority(Role.ADMIN.name))
        val member = MemberFixture.aMember(
            id = 1L,
            name = "test_admin",
            token = MemberConstant.TEST_ADMIN_TOKEN.name,
            role = Role.ADMIN
        )
        val authentication = UsernamePasswordAuthenticationToken(
            member,
            null,
            member.authorities
        )
        context.authentication = authentication

        return context
    }
}