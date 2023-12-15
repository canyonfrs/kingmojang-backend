package com.app.canyonfrs.kingmojang.member

data class MemberResponse(
    val id: Long,
    val email: String,
    val name: String,
    val token: String,
    val phoneNumber: String? = null,
    val role: Role,
) {
    companion object {
        fun of(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id!!,
                email = member.email,
                name = member.name,
                token = member.token,
                phoneNumber = member.phoneNumber,
                role = member.role,
            )
        }
    }
}