package com.ribbontek.util

import com.ribbontek.Colors
import com.ribbontek.PieDataSet

object PieDataSetFactory {
    fun pieDataSet(
        label: String = FakerUtil.alphaNumeric(10),
        value: Double = FakerUtil.randomDouble(),
        color: String = FakerUtil.enum<Colors>().toString()
    ): PieDataSet {
        return PieDataSet(label = label, value = value, color = color)
    }
}
