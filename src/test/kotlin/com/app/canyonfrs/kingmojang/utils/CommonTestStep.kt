package com.app.canyonfrs.kingmojang.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.test.web.servlet.ResultActions

inline fun <reified T> ResultActions.toResponse(objectMapper: ObjectMapper): T{
    return objectMapper.readValue(this.andReturn().response.contentAsString, T::class.java)
}