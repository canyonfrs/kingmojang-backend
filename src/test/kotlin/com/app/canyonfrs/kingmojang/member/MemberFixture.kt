package com.app.canyonfrs.kingmojang.member

import com.app.canyonfrs.kingmojang.utils.RandomGenerator

class MemberFixture {
    companion object {
        fun aMember(
            email: String = "${RandomGenerator.randomString(10)}@gmail.com",
            name: String = RandomGenerator.randomString(10),
            token: String = RandomGenerator.randomString(100),
            phoneNumber: String? = "${RandomGenerator.randomString(3)}-${
                RandomGenerator.randomString(
                    4
                )
            }-${RandomGenerator.randomString(4)}",
            role: Role = Role.STREAMER,
            id: Long? = 1L,
        ): Member {

            return Member(
                email = email,
                name = name,
                token = token,
                phoneNumber = phoneNumber,
                role = role,
                id = id
            )
        }
    }
}