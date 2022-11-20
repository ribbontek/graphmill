package com.ribbontek

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt


data class PieDataSet(
    val label: String,
    val value: Double,
    val color: String
)

fun pieChart(instance: PieChart.() -> Unit): PieChart {
    return PieChart().apply { instance(this) }
}

class PieChart {
    var width: Int = -1
    var height: Int = -1
    var dataSet: List<PieDataSet> = emptyList()

    fun renderToFile(
        directory: String = "",
        fileName: String = System.currentTimeMillis().toString()
    ) {
        render().toFile(directory, fileName)
    }

    private fun render(): BufferedImage {
        // validate pie chart data
        validate()
        // set up graphics 2d
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g2: Graphics2D = image.createGraphics()
        // set up background
        g2.color = Color.WHITE;
        g2.fillRect(0, 0, width, height)
        // set up pie chart boundaries
        val pieWidth = (width * 0.9).roundToInt()
        val pieHeight = (height * 0.9).roundToInt()
        val pieX = (height - pieWidth) / 2
        val pieY = (width - pieHeight) / 2
        // draw pie chart
        val total = dataSet.sumOf { it.value }
        var currentValue = 0.0
        dataSet.forEach {
            val startAngle = (currentValue * 360 / total).roundToInt()
            val arcAngle = (it.value * 360 / total).roundToInt()
            g2.color = Colors.getColor(it.color)
            g2.fillArc(pieX, pieY, pieWidth, pieHeight, startAngle, arcAngle)
            currentValue += it.value
        }
        return image
    }

    private fun validate() {
        assert(width > 0) { "Width for Pie Chart must be greater than 0" }
        assert(height > 0) { "Height for Pie Chart must be greater than 0" }
        assert(dataSet.isNotEmpty()) { "Data Set for Pie Chart must not be empty" }
    }

    private fun BufferedImage.toFile(directory: String, fileName: String) {
        val file = File(directory, "/${fileName}.jpg")
        file.parentFile.mkdirs()
        file.writeBytes(ByteArray(0))
        ImageIO.write(this, "jpg", file)
    }
}
