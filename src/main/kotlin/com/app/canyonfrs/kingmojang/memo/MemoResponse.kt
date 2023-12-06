package com.app.canyonfrs.kingmojang.memo

import java.time.LocalDateTime

data class MemoResponse(
    val memoId: Long,
    val content: String,
    val writerId: Long,
    val visibility: Visibility,
    val createdDateTime: LocalDateTime,
    val updatedDateTime: LocalDateTime
) {

    companion object {
        fun from(memo: Memo) = MemoResponse(
            memoId = memo.id!!,
            content = memo.content,
            writerId = memo.writerId,
            visibility = memo.visibility,
            createdDateTime = memo.createdDateTime,
            updatedDateTime = memo.updatedDateTime
        )
    }
}