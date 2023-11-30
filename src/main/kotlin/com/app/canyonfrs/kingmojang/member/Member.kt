package com.app.canyonfrs.kingmojang.member

import com.app.canyonfrs.kingmojang.common.ActiveStatus
import com.app.canyonfrs.kingmojang.common.BaseEntity
import com.app.canyonfrs.kingmojang.memo.Memo
import com.app.canyonfrs.kingmojang.memo.Visibility
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
data class Member(
    var email: String,
    var name: String,
    // TODO: 시청자가 생기면 어떻게 다룰건지 고민하기(getPassword)
    var token: String,
    var phoneNumber: String? = null,
    @Enumerated(EnumType.STRING)
    var role: Role,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
) : BaseEntity(), UserDetails {

    fun createEmptyMemo(): Memo {
        if (role.name != "STREAMER") {
            throw IllegalArgumentException("메모 생성은 스트리머만 가능합니다.")
        }

        return Memo(
            content = "",
            writerId = this.id!!,
            visibility = Visibility.PUBLIC,
        )
    }


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.name))
    }

    override fun getPassword(): String {
        return token!!
    }

    override fun getUsername(): String {
        return token!!;
    }

    override fun isAccountNonExpired(): Boolean {
        return activeStatus == ActiveStatus.ACTIVE
    }

    override fun isAccountNonLocked(): Boolean {
        return activeStatus == ActiveStatus.ACTIVE
    }

    override fun isCredentialsNonExpired(): Boolean {
        return activeStatus == ActiveStatus.ACTIVE
    }

    override fun isEnabled(): Boolean {
        return activeStatus == ActiveStatus.ACTIVE
    }
}