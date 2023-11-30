package com.app.canyonfrs.kingmojang.memo

data class MemoCursorPageCondition(
    val lastCursorId: Long,
    val pageSize: Int,
    val streamerId: Long? = null,
) {

    companion object {
        fun of(
            lastCursorId: Long,
            pageSize: Int,
            memberId: Long? = null,
        ): MemoCursorPageCondition {
            return MemoCursorPageCondition(
                lastCursorId = lastCursorId,
                pageSize = pageSize,
                streamerId = memberId,
            )
        }
    }
}
