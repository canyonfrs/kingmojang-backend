package com.app.canyonfrs.kingmojang.common

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.UNAUTHORIZED

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val detail: String
) {
    /* 401 UNAUTHORIZED: */
    INVALID_VERIFICATION_CODE(UNAUTHORIZED, "유효하지 않은 인증 코드입니다."),
    INVALID_MEMBER_STATUS(UNAUTHORIZED, "계정 상태가 비활성화 상태입니다."),
}