package com.app.canyonfrs.kingmojang.memo

import com.app.canyonfrs.kingmojang.common.BaseEntity
import com.app.canyonfrs.kingmojang.member.Member
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
class Memo(
    @Comment("메모 내용")
    @Column(columnDefinition = "TEXT")
    var content: String,
    @Comment("메모 작성자 id. member.id와 연결됨")
    val writerId: Long,
    @Comment("메모 공개 여부")
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
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