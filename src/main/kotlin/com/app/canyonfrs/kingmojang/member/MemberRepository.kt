package com.app.canyonfrs.kingmojang.member

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByToken(token: String): Member
}