package com.app.canyonfrs.kingmojang.utils

import kotlin.random.Random

class RandomGenerator {
    companion object {
        fun randomString(length: Int): String {
            val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            return (1..length)
                .map { kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")
        }

        fun randomDigit(length: Int): String {
            return (1..length)
                .map { Random.nextInt(0, 10)}
                .joinToString("")
        }
    }
}