package com.app.canyonfrs.kingmojang.member

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository {
    fun findByToken(token: String): Member
}

interface MemberJpaRepository : MemberRepository, JpaRepository<Member, Long> {
    override fun findByToken(token: String): Member
}