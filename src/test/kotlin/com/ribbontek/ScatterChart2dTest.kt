package com.ribbontek

import com.ribbontek.chart.ScatterChart2d
import com.ribbontek.style.Colors
import com.ribbontek.util.ScatterDataSetFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ScatterChart2dTest : AbstractChartTest() {

    @Test
    fun `render scatter chart to file (500x500) - success`() {
        createScatterChart2d(500, 500).renderToFile(BUILD_DIR)
    }

    @Test
    fun `render scatter chart to file (1000x1000) - success`() {
        createScatterChart2d(1000, 1000).renderToFile(BUILD_DIR)
    }

    @Test
    fun `render scatter chart to file gpu enhanced - success`() {
        createScatterChart2d(1000, 1000).renderToFileGpuEnhanced(BUILD_DIR)
    }

    @Test
    fun `render scatter chart to file - fail`() {
        assertThrows<AssertionError> {
            ScatterChart2d().renderToFile(BUILD_DIR)
        }
    }

    private fun createScatterChart2d(width: Int, height: Int): ScatterChart2d {
        return Graphmill.scatterChart2d {
            title = "Pretty Colors"
            subtitle = "Generated with graphmill"
            this.width = width
            this.height = height
            dataSet = listOf(
                ScatterDataSetFactory.scatterDataSet(color = Colors.RED.toString()),
                ScatterDataSetFactory.scatterDataSet(color = Colors.YELLOW.toString()),
                ScatterDataSetFactory.scatterDataSet(color = Colors.GREEN.toString())
            )
        }
    }
}
