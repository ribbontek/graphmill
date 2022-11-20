package com.ribbontek

import com.ribbontek.style.Colors
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import kotlin.math.ceil
import kotlin.math.roundToInt

data class PieDataSet(
    val label: String,
    val value: Double,
    val color: String
)

fun pieChart2d(instance: PieChart2d.() -> Unit): PieChart2d {
    return PieChart2d().apply { instance(this) }
}

class PieChart2d : AbstractChart() {
    var width: Int = -1
    var height: Int = -1
    var dataSet: List<PieDataSet> = emptyList()
    var backgroundColor: Color = Color.WHITE
    var title: String? = null
    var subtitle: String? = null

    override fun render(): BufferedImage {
        // validate pie chart data
        validate()
        // set up image
        val image = setUpImage(width, height, backgroundColor)
        // set up graphics
        val g2: Graphics2D = image.createGraphics()
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        // set up pie chart boundaries
        val pieWidth = (width * 0.7).roundToInt()
        val pieHeight = (height * 0.7).roundToInt()
        val pieX = (height - pieWidth) / 2
        val pieY = (width - pieHeight) / 2
        // draw pie chart
        val total = dataSet.sumOf { it.value }
        var currentValue = 0.0
        dataSet.forEach {
            val startAngle = ceil(currentValue * 360 / total).roundToInt()
            val arcAngle = ceil(it.value * 360 / total).roundToInt()
            g2.color = Colors.getColor(it.color)
            g2.fillArc(pieX, pieY, pieWidth, pieHeight, startAngle, arcAngle)
            currentValue += it.value
        }
        // draw title
        title?.takeIf { it.isNotBlank() }?.let {
            drawTextCentered(g2, width, height, it, 24, 0.95)
        }
        // draw subtitle
        subtitle?.takeIf { it.isNotBlank() }?.let {
            drawTextCentered(g2, width, height, it, 12, 0.90)
        }

        return image
    }

    private fun validate() {
        assert(width > 0) { "Width for ${this::class.simpleName} must be greater than 0" }
        assert(height > 0) { "Height for ${this::class.simpleName} must be greater than 0" }
        assert(dataSet.isNotEmpty()) { "Data Set for ${this::class.simpleName} must not be empty" }
    }
}
