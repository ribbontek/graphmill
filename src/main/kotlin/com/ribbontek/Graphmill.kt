package com.ribbontek

import com.ribbontek.chart.BarChart2d
import com.ribbontek.chart.LineChart2d
import com.ribbontek.chart.PieChart2d
import com.ribbontek.chart.ScatterChart2d

object Graphmill {

    fun barChart2d(instance: BarChart2d.() -> Unit): BarChart2d {
        return BarChart2d().apply { instance() }
    }

    fun pieChart2d(instance: PieChart2d.() -> Unit): PieChart2d {
        return PieChart2d().apply { instance() }
    }

    fun lineChart2d(instance: LineChart2d.() -> Unit): LineChart2d {
        return LineChart2d().apply { instance() }
    }

    fun scatterChart2d(instance: ScatterChart2d.() -> Unit): ScatterChart2d {
        return ScatterChart2d().apply { instance() }
    }
}
