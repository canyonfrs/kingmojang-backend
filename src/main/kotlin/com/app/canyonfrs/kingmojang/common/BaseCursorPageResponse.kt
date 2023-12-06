package com.app.canyonfrs.kingmojang.common

abstract class BaseCursorPageResponse<T> (
    open val data: List<T>,
    open val hasNext: Boolean,
    open val nextCursorId: Long,
    open val isEmpty: Boolean,
)