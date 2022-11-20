package com.ribbontek.util

import com.github.javafaker.Faker
import org.apache.commons.lang3.RandomUtils

object FakerUtil {
    private val faker = Faker()

    fun resourceId() =
        faker.number().numberBetween(1, 10000).toString()

    fun alphaNumeric(maxSize: Int): String =
        faker.lorem().characters(maxSize)

    fun randomNumber(minNumber: Long = 0, maxNumber: Long = Long.MAX_VALUE) =
        faker.number().numberBetween(minNumber, maxNumber)

    fun randomDouble(decimals: Int = 2, minNumber: Long = 0, maxNumber: Long = Long.MAX_VALUE) =
        faker.number().randomDouble(decimals, minNumber, maxNumber)

    inline fun <reified T : Enum<*>> enum(): T =
        T::class.java.enumConstants[RandomUtils.nextInt(0, T::class.java.enumConstants.size)]
}
