package com.app.canyonfrs.kingmojang.memo

import com.app.canyonfrs.kingmojang.common.BaseCursorPageResponse

data class MemoCursorPageResponse(
    override val data: List<MemoResponse>,
    override val hasNext: Boolean,
    override val nextCursorId: Long,
    override val isEmpty: Boolean,
) : BaseCursorPageResponse<MemoResponse>(
    data = data,
    hasNext = hasNext,
    nextCursorId = nextCursorId,
    isEmpty = isEmpty,
) {
    companion object {
        fun of(memos: List<Memo>, pageSize: Int): MemoCursorPageResponse {
            return MemoCursorPageResponse(
                data = memos.map { MemoResponse.from(it) },
                hasNext = memos.size == pageSize,
                nextCursorId = if (memos.isEmpty()) -1 else memos.get(memos.size - 1).id!!,
                isEmpty = memos.isEmpty(),
            )
        }
    }
}