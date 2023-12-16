package com.app.canyonfrs.kingmojang.common

class BusinessException(
    val errorCode: ErrorCode,
) : RuntimeException()