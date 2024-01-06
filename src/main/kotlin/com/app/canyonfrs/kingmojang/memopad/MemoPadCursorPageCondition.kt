package com.app.canyonfrs.kingmojang.memopad

data class MemoPadCursorPageCondition(
    val lastCursorId: Long,
    val pageSize: Int,
    val streamerId: Long? = null,
) {

    companion object {
        fun of(
            lastCursorId: Long,
            pageSize: Int,
            memberId: Long? = null,
        ): MemoPadCursorPageCondition {
            return MemoPadCursorPageCondition(
                lastCursorId = lastCursorId,
                pageSize = pageSize,
                streamerId = memberId,
            )
        }
    }
}
