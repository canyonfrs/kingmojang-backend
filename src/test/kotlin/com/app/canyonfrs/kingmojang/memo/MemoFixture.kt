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
    }
}