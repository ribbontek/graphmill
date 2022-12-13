package com.ribbontek

import com.ribbontek.chart.BarChart2d
import com.ribbontek.style.Colors
import com.ribbontek.util.BarDataSetFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BarChart2dTest : AbstractChartTest() {

    @Test
    fun `bar chart - success`() {
        createBarChart2d().renderToFile(BUILD_DIR)
    }

    @Test
    fun `bar chart - fail`() {
        assertThrows<AssertionError> {
            BarChart2d().renderToFile(BUILD_DIR)
        }
    }

    private fun createBarChart2d(): BarChart2d {
        return Graphmill.barChart2d {
            title = "Pretty Colors"
            subtitle = "Generated with graphmill"
            width = 500
            height = 500
            dataSet = listOf(
                BarDataSetFactory.barDataSet(color = Colors.RED.toString()),
                BarDataSetFactory.barDataSet(color = Colors.PINK.toString()),
                BarDataSetFactory.barDataSet(color = Colors.ORANGE.toString()),
                BarDataSetFactory.barDataSet(color = Colors.YELLOW.toString()),
                BarDataSetFactory.barDataSet(color = Colors.GREEN.toString()),
                BarDataSetFactory.barDataSet(color = Colors.BLUE.toString()),
                BarDataSetFactory.barDataSet(color = Colors.CYAN.toString()),
                BarDataSetFactory.barDataSet(color = Colors.MAGENTA.toString()),
            )
        }
    }
}
