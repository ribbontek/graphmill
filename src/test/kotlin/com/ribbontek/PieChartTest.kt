package com.ribbontek

import com.ribbontek.util.PieDataSetFactory
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PieChartTest {

    private lateinit var pieChart: PieChart

    @BeforeAll
    fun setUp() {
        pieChart = PieChart()
    }

    @Test
    fun `pie chart`() {
        createPieChart().renderToFile("build")
    }

    private fun createPieChart(): PieChart {
        return pieChart {
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

    private fun renderTest() {
        val imageBase = BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB)
        val g2: Graphics2D = imageBase.createGraphics()
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
        g2.font = Font("Serif", Font.PLAIN, 24)
        g2.drawString("Welcome to TutorialsPoint", 50, 70)
        // imageBase.toFile("/build", System.currentTimeMillis().toString())
    }
}
