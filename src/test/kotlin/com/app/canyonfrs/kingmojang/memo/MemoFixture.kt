package com.app.canyonfrs.kingmojang.memo

import com.app.canyonfrs.kingmojang.utils.RandomGenerator.Companion.randomString

class MemoFixture {
    companion object {
        fun aMemo(
            content: String = randomString(10),
            writerId: Long = 1L,
            visibility: Visibility = Visibility.PUBLIC,
            id: Long? = 1L
        ): Memo {
            return Memo(
                content = content,
                writerId = writerId,
                visibility = visibility,
                id = id
            )
        }

        fun memos(
            size: Int = 20,
            writerId: Long = 1L,
            visibility: Visibility = Visibility.PUBLIC,
        ): List<Memo> {

            return (1..size).map {
                aMemo(
                    writerId = writerId,
                    visibility = visibility,
                    id = null
                )
            }
        }
    }
}