package com.app.canyonfrs.kingmojang.memopad

import com.app.canyonfrs.kingmojang.common.BaseCursorPageResponse

data class MemoPadCursorPageResponse(
    override val data: List<MemoPadResponse>,
    override val hasNext: Boolean,
    override val nextCursorId: Long,
    override val isEmpty: Boolean,
) : BaseCursorPageResponse<MemoPadResponse>(
    data = data,
    hasNext = hasNext,
    nextCursorId = nextCursorId,
    isEmpty = isEmpty,
) {
    companion object {
        fun of(memoPads: List<MemoPad>, pageSize: Int): MemoPadCursorPageResponse {
            return MemoPadCursorPageResponse(
                data = memoPads.map { MemoPadResponse.from(it) },
                hasNext = memoPads.size == pageSize,
                nextCursorId = if (memoPads.isEmpty()) -1 else memoPads.get(memoPads.size - 1).id!!,
                isEmpty = memoPads.isEmpty(),
            )
        }
    }
}