package com.app.canyonfrs.kingmojang.member

import com.app.canyonfrs.kingmojang.common.ActiveStatus
import com.app.canyonfrs.kingmojang.common.BaseEntity
import com.app.canyonfrs.kingmojang.member.TokenGeneratorImpl.Companion.TOKEN_LENGTH
import com.app.canyonfrs.kingmojang.memo.Memo
import com.app.canyonfrs.kingmojang.memo.Visibility
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
data class Member(
    @Comment("사용자 이메일")
    @Column(length = 254, nullable = false, unique = true)
    var email: String,
    @Comment("사용자 이름 혹은 닉네임")
    @Column(length = 30)
    var name: String,
    @Comment("사용자 고유 토큰. 현재 비밀번호처럼 사용중")
    @Column(length = TOKEN_LENGTH, unique = true)
    var token: String,
    @Comment("사용자 전화번호. xxx-xxxx-xxxx 형식")
    @Column(length = 13)
    var phoneNumber: String? = null,
    @Comment("사용자 활성화 여부")
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
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

    fun createMember(memberRequest: MemberRequest, tokenGenerator: TokenGenerator): Member {
        return createMember(
            memberRequest.email,
            memberRequest.name,
            memberRequest.phoneNumber,
            memberRequest.role,
            tokenGenerator
        )
    }

    fun createMember(
        email: String,
        name: String,
        phoneNumber: String?,
        role: Role,
        tokenGenerator: TokenGenerator
    ): Member {
        if (!isValidEmail(email)) {
            throw IllegalArgumentException("이메일 형식이 올바르지 않습니다. 입력받은 이메일: $email")
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            throw IllegalArgumentException("전화번호 형식이 올바르지 않습니다. 입력받은 전화번호: $phoneNumber")
        }

        if (this.role.name != "ADMIN") {
            throw IllegalArgumentException("회원 생성은 관리자만 가능합니다.")
        }

        return Member(
            email = email,
            name = name,
            token = tokenGenerator.generateToken(),
            phoneNumber = phoneNumber,
            role = role,
        )
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9](.*)([@]{1})(.{1,})(\\.)(.{1,})$"
        return email.matches(emailRegex.toRegex())
    }

    private fun isValidPhoneNumber(phoneNumber: String?): Boolean {
        if (phoneNumber.isNullOrBlank()) return true;

        val phoneNumberRegex = "^\\d{3}-\\d{3,4}-\\d{4}$"
        return phoneNumber.matches(phoneNumberRegex.toRegex())
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