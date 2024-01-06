package com.app.canyonfrs.kingmojang.memopad

import com.app.canyonfrs.kingmojang.member.Member
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
class MemoPadController(
    val memoPadService: MemoPadService,
) {

    @PostMapping("/api/v1/memo-pads")
    fun createMemoPad(@AuthenticationPrincipal member: Member): ResponseEntity<MemoPadResponse> {
        val response = memoPadService.createMemo(member)

        return ResponseEntity.created(URI("/api/v1/memo-pads/${response.memoPadId}")).body(response)
    }

    @PutMapping("/api/v1/memo-pads/{memoPadId}")
    fun updateMemo(
        @AuthenticationPrincipal member: Member,
        @PathVariable memoPadId: Long,
        @RequestBody memoPadRequest: MemoPadRequest
    ): ResponseEntity<MemoPadResponse> {
        val response = memoPadService.updateMemoPad(member, memoPadId, memoPadRequest)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/v1/memo-pads/{memoId}")
    fun getMemo(
        @PathVariable memoId: Long
    ): ResponseEntity<MemoPadResponse> {
        val response = memoPadService.getMemoPad(memoId)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/v1/memo-pads")
    fun getMemoPads(
        @RequestParam streamerId: Long? = null,
        @RequestParam lastCursorId: Long = Long.MAX_VALUE,
        @RequestParam pageSize: Int = 20
    ): ResponseEntity<MemoPadCursorPageResponse> {
        val response = memoPadService.getMemoPads(MemoPadCursorPageCondition.of(lastCursorId, pageSize, streamerId))

        return ResponseEntity.ok(response)
    }
}