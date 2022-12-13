package com.ribbontek.util

import com.ribbontek.chart.ScatterDataSet
import com.ribbontek.style.Colors

object ScatterDataSetFactory {
    fun scatterDataSet(
        label: String = FakerUtil.alphaNumeric(10),
        color: String = FakerUtil.enum<Colors>().toString(),
        generator: () -> Double = { FakerUtil.randomDouble(minNumber = -100, maxNumber = 100) }
    ): ScatterDataSet {
        return ScatterDataSet(
            label = label,
            data = (0 until FakerUtil.randomNumber(minNumber = 10, maxNumber = 20)).map {
                ScatterDataSet.Coordinate(
                    generator(),
                    generator()
                )
            },
            color = color
        )
    }
}
