package com.ribbontek

import com.ribbontek.chart.ScatterChart2d
import com.ribbontek.style.Colors
import com.ribbontek.util.ScatterDataSetFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ScatterChart2dTest : AbstractChartTest() {

    @Test
    fun `scatter chart - success`() {
        createScatterChart2d().renderToFile(BUILD_DIR)
    }

    @Test
    fun `scatter chart - fail`() {
        assertThrows<AssertionError> {
            ScatterChart2d().renderToFile(BUILD_DIR)
        }
    }

    private fun createScatterChart2d(): ScatterChart2d {
        return Graphmill.scatterChart2d {
            title = "Pretty Colors"
            subtitle = "Generated with graphmill"
            width = 500
            height = 500
            dataSet = listOf(
                ScatterDataSetFactory.scatterDataSet(color = Colors.RED.toString()),
                ScatterDataSetFactory.scatterDataSet(color = Colors.YELLOW.toString()),
                ScatterDataSetFactory.scatterDataSet(color = Colors.GREEN.toString())
            )
        }
    }
}
