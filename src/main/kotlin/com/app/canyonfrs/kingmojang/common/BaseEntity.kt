package com.app.canyonfrs.kingmojang.common

import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    val createdDateTime: LocalDateTime = LocalDateTime.now()
    @LastModifiedDate
    var updatedDateTime: LocalDateTime = LocalDateTime.now()
        protected set
    @Enumerated(EnumType.STRING)
    val activeStatus: ActiveStatus = ActiveStatus.ACTIVE
}