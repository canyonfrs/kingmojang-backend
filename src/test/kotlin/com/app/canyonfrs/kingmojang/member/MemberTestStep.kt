package com.app.canyonfrs.kingmojang.member

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

fun MockMvc.createMember(adminToken: String, content: String): ResultActions {
    return this.perform(
        post("/api/v1/members")
            .header("Authorization", "Bearer $adminToken")
            .contentType("application/json")
            .content(content)
    )
}