package com.app.canyonfrs.kingmojang.memo

import com.app.canyonfrs.kingmojang.common.BaseEntity
import com.app.canyonfrs.kingmojang.member.Member
import jakarta.persistence.*

@Entity
class Memo(
    var content: String,
    val writerId: Long,
    @Enumerated(EnumType.STRING)
    var visibility: Visibility,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) : BaseEntity() {

    fun update(member: Member, content: String, visibility: Visibility) {
        if (writerId != member.id) {
            throw IllegalArgumentException("요청한 메모의 작성자가 아닙니다.")
        }

        this.content = content
        this.visibility = visibility
    }
}