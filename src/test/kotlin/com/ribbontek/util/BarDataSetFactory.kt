package com.ribbontek.util

import com.ribbontek.BarDataSet
import com.ribbontek.style.Colors

object BarDataSetFactory {
    fun barDataSet(
        label: String = FakerUtil.alphaNumeric(10),
        value: Double = FakerUtil.randomDouble(maxNumber = 10000),
        color: String = FakerUtil.enum<Colors>().toString()
    ): BarDataSet {
        return BarDataSet(label = label, value = value, color = color)
    }
}
