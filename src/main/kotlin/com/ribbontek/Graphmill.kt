package com.ribbontek

import com.ribbontek.chart.BarChart2d
import com.ribbontek.chart.PieChart2d

object Graphmill {

    fun barChart2d(instance: BarChart2d.() -> Unit): BarChart2d {
        return BarChart2d().apply { instance(this) }
    }

    fun pieChart2d(instance: PieChart2d.() -> Unit): PieChart2d {
        return PieChart2d().apply { instance(this) }
    }
}
