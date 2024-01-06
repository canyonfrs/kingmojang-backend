package com.app.canyonfrs.kingmojang.memopad

import com.app.canyonfrs.kingmojang.utils.RandomGenerator.Companion.randomString

class MemoPadFixture {
    companion object {
        fun aMemoPad(
            content: String = randomString(10),
            writerId: Long = 1L,
            visibility: Visibility = Visibility.PUBLIC,
            id: Long? = null
        ): MemoPad {
            return MemoPad(
                content = content,
                writerId = writerId,
                visibility = visibility,
                id = id
            )
        }

        fun memoPads(
            size: Int = 20,
            writerId: Long = 1L,
            visibility: Visibility = Visibility.PUBLIC,
        ): List<MemoPad> {

            return (1..size).map {
                aMemoPad(
                    writerId = writerId,
                    visibility = visibility,
                    id = null
                )
            }
        }
    }
}