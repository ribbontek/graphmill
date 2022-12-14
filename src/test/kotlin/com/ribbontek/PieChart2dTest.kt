package com.ribbontek

import com.ribbontek.chart.PieChart2d
import com.ribbontek.style.Colors
import com.ribbontek.util.PieDataSetFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PieChart2dTest : AbstractChartTest() {

    @Test
    fun `render pie chart to file (500x500) - success`() {
        createPieChart2d(500, 500).renderToFile(BUILD_DIR)
    }

    @Test
    fun `render pie chart to file (1000x1000) - success`() {
        createPieChart2d(1000, 1000).renderToFile(BUILD_DIR)
    }

    @Test
    fun `render pie chart to file - fail`() {
        assertThrows<AssertionError> {
            PieChart2d().renderToFile(BUILD_DIR)
        }
    }

    private fun createPieChart2d(width: Int, height: Int): PieChart2d {
        return Graphmill.pieChart2d {
            title = "Pretty Colors"
            subtitle = "Generated with graphmill"
            this.width = width
            this.height = height
            displayOutline = true
            dataSet = listOf(
                PieDataSetFactory.pieDataSet(color = Colors.RED.toString()),
                PieDataSetFactory.pieDataSet(color = Colors.PINK.toString()),
                PieDataSetFactory.pieDataSet(color = Colors.ORANGE.toString()),
                PieDataSetFactory.pieDataSet(color = Colors.YELLOW.toString()),
                PieDataSetFactory.pieDataSet(color = Colors.GREEN.toString())
            )
        }
    }
}
