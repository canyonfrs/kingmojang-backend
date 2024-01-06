package com.app.canyonfrs.kingmojang.member

import com.app.canyonfrs.kingmojang.utils.RandomGenerator

class MemberFixture {
    companion object {
        fun aMember(
            email: String = "${RandomGenerator.randomString(10)}@gmail.com",
            name: String = RandomGenerator.randomString(10),
            token: String = RandomGenerator.randomString(100),
            phoneNumber: String? = "${RandomGenerator.randomDigit(3)}-${
                RandomGenerator.randomDigit(
                    4
                )
            }-${RandomGenerator.randomDigit(4)}",
            role: Role = Role.STREAMER,
            id: Long? = null,
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

        fun aMemberRequest(
            email: String = "${RandomGenerator.randomString(10)}@gmail.com",
            name: String = RandomGenerator.randomDigit(10),
            phoneNumber: String? = "${RandomGenerator.randomDigit(3)}-${
                RandomGenerator.randomDigit(
                    4
                )
            }-${RandomGenerator.randomDigit(4)}",
            role: Role = Role.STREAMER,
        ): MemberRequest {
           return MemberRequest(
               email = email,
               name = name,
               phoneNumber = phoneNumber,
               role = role,
           )
        }
    }
}