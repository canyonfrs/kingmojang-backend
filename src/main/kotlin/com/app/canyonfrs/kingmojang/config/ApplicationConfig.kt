package com.app.canyonfrs.kingmojang.config

import com.app.canyonfrs.kingmojang.member.MemberRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService

@Configuration
class ApplicationConfig(
    private val memberRepository: MemberRepository
) {

    @Bean
    fun userDetailsService() = UserDetailsService { token -> memberRepository.findByToken(token) }

    @Bean
    fun authenticationProvider() : AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService())

        return authProvider
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration) : AuthenticationManager {
        return config.authenticationManager
    }
}