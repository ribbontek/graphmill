package com.ribbontek

import com.ribbontek.chart.LineChart2d
import com.ribbontek.style.Colors
import com.ribbontek.util.LineDataSetFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

class LineChart2dTest : AbstractChartTest() {

    @Test
    fun `line chart - success`() {
        createLineChart2d().renderToFile(BUILD_DIR)
    }

    @Test
    fun `line chart - fail`() {
        assertThrows<AssertionError> {
            LineChart2d().renderToFile(BUILD_DIR)
        }
    }

    private fun createLineChart2d(): LineChart2d {
        return Graphmill.lineChart2d {
            title = "Pretty Colors"
            subtitle = "Generated with graphmill"
            width = 500
            height = 500
            labels = Month.values().map { it.getDisplayName(TextStyle.FULL_STANDALONE, Locale.US) }
            dataSet = listOf(
                LineDataSetFactory.lineDataSet(size = 12, color = Colors.RED.toString()),
                LineDataSetFactory.lineDataSet(color = Colors.YELLOW.toString()),
                LineDataSetFactory.lineDataSet(size = 14, color = Colors.GREEN.toString())
            )
        }
    }
}
