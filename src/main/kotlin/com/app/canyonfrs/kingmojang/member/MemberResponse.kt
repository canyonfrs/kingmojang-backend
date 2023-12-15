package com.app.canyonfrs.kingmojang.member

data class MemberResponse(
    val id: Long,
    val email: String,
    val name: String,
    val token: String,
    val phoneNumber: String? = null,
    val role: Role,
)