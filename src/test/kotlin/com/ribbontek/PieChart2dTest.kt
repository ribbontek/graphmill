package com.ribbontek

import com.ribbontek.style.Colors
import com.ribbontek.util.PieDataSetFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PieChart2dTest {

    companion object {
        private const val BUILD_DIR = "build/charts"
    }

    @Test
    fun `pie chart - success`() {
        createPieChart2d().renderToFile(BUILD_DIR)
    }

    @Test
    fun `pie chart - fail`() {
        assertThrows<AssertionError> {
            PieChart2d().renderToFile(BUILD_DIR)
        }
    }

    private fun createPieChart2d(): PieChart2d {
        return pieChart2d {
            title = "Pretty Colors"
            subtitle = "Generated with graphmill"
            width = 500
            height = 500
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
