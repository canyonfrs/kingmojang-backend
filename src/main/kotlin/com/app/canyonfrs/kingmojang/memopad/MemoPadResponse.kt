package com.app.canyonfrs.kingmojang.memopad

import java.time.LocalDateTime

data class MemoPadResponse(
    val memoPadId: Long,
    val content: String,
    val writerId: Long,
    val visibility: Visibility,
    val createdDateTime: LocalDateTime,
    val updatedDateTime: LocalDateTime
) {

    companion object {
        fun from(memoPad: MemoPad) = MemoPadResponse(
            memoPadId = memoPad.id!!,
            content = memoPad.content,
            writerId = memoPad.writerId,
            visibility = memoPad.visibility,
            createdDateTime = memoPad.createdDateTime,
            updatedDateTime = memoPad.updatedDateTime
        )
    }
}