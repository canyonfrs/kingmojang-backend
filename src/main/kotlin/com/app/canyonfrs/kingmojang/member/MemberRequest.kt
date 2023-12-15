package com.app.canyonfrs.kingmojang.member

data class MemberRequest(
    val email: String,
    val name: String,
    val phoneNumber: String? = null,
    val role: Role,
)
