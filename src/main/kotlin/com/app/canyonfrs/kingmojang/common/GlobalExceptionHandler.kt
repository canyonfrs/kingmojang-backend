package com.app.canyonfrs.kingmojang.common

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(BusinessException::class)
    protected fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.from(e.errorCode)
    }
}