package com.ribbontek.util

import com.ribbontek.chart.PieDataSet

object PieDataSetFactory {
    fun pieDataSet(
        label: String = FakerUtil.alphaNumeric(10),
        value: Double = FakerUtil.randomDouble(maxNumber = 10000),
        color: String = FakerUtil.enum<Colors>().toString()
    ): PieDataSet {
        return PieDataSet(label = label, value = value, color = color)
    }
}
