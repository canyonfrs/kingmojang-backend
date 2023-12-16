package com.app.canyonfrs.kingmojang.member

import org.springframework.stereotype.Component

fun interface TokenGenerator {
    fun generateToken(): String
}

@Component
class TokenGeneratorImpl : TokenGenerator {
    companion object {
        const val TOKEN_LENGTH = 255
    }

    override fun generateToken(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..TOKEN_LENGTH)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}

