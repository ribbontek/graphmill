package com.ribbontek.util

import com.ribbontek.chart.LineDataSet

object LineDataSetFactory {
    fun lineDataSet(
        label: String = FakerUtil.alphaNumeric(10),
        size: Int = 10,
        generator: () -> Double = { FakerUtil.randomDouble(maxNumber = 10000) },
        color: String = FakerUtil.enum<Colors>().toString()
    ): LineDataSet {
        return LineDataSet(label = label, values = (0 until size).map { generator() }, color = color)
    }
}
