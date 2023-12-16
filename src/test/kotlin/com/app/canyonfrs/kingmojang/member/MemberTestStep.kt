package com.app.canyonfrs.kingmojang.member

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

fun MockMvc.createMember(adminToken: String, content: String): ResultActions {
    return this.perform(
        post("/api/v1/members")
            .header("Authorization", "Bearer $adminToken")
            .contentType("application/json")
            .content(content)
    )
}

fun MockMvc.validateVerificationCode(verificationCode: String): ResultActions {
    return this.perform(
        get("/api/v1/members/validate-verification-code")
            .header("Authorization", "Bearer $verificationCode")
            .contentType("application/json")
    )
}