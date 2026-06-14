package com.example.pixelpet.avatar

import kotlin.random.Random

interface AvatarRandom {
    fun nextLong(fromInclusive: Long, toInclusive: Long): Long
    fun nextFloat(): Float
}

class KotlinAvatarRandom(
    private val random: Random = Random.Default,
) : AvatarRandom {
    override fun nextLong(fromInclusive: Long, toInclusive: Long): Long {
        require(fromInclusive <= toInclusive) { "Invalid random range." }
        return random.nextLong(fromInclusive, toInclusive + 1L)
    }

    override fun nextFloat(): Float = random.nextFloat()
}
