package com.app.canyonfrs.kingmojang.memo

import com.app.canyonfrs.kingmojang.common.BaseCursorPageResponse
import com.app.canyonfrs.kingmojang.member.Member
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
class MemoController(
    val memoService: MemoService,
) {

    @PostMapping("/api/v1/memos")
    fun createNote(@AuthenticationPrincipal member: Member): ResponseEntity<MemoResponse> {
        val response = memoService.createMemo(member)

        return ResponseEntity.created(URI("/api/v1/memos/${response.memoId}")).body(response)
    }

    @PutMapping("/api/v1/memos/{memoId}")
    fun updateMemo(
        @AuthenticationPrincipal member: Member,
        @PathVariable memoId: Long,
        @RequestBody memoRequest: MemoRequest
    ): ResponseEntity<MemoResponse> {
        val response = memoService.updateMemo(member, memoId, memoRequest)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/v1/memos/{memoId}")
    fun getMemo(
        @PathVariable memoId: Long
    ): ResponseEntity<MemoResponse> {
        val response = memoService.getMemo(memoId)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/v1/memos")
    fun getMemos(
        @RequestParam streamerId: Long? = null,
        @RequestParam lastCursorId: Long = Long.MAX_VALUE,
        @RequestParam pageSize: Int = 20
    ): ResponseEntity<MemoCursorPageResponse> {
        val response = memoService.getMemos(MemoCursorPageCondition.of(lastCursorId, pageSize, streamerId))

        return ResponseEntity.ok(response)
    }
}