package com.ribbontek.chart

import com.ribbontek.util.Colors
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.geom.Arc2D
import java.awt.image.BufferedImage
import kotlin.math.ceil
import kotlin.math.roundToInt

data class PieDataSet(
    val label: String,
    val value: Double,
    val color: String
) : DataSet

class PieChart2d(
    override var width: Int = -1,
    override var height: Int = -1,
    override var dataSet: List<PieDataSet> = emptyList(),
    var backgroundColor: Color = Color.WHITE,
    var title: String? = null,
    var subtitle: String? = null,
    var displayOutline: Boolean = false
) : AbstractChart<PieDataSet>() {

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
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)
        // set up pie chart boundaries
        val pieWidth = (width * 0.7).roundToInt()
        val pieHeight = (height * 0.7).roundToInt()
        val pieX = (height - pieWidth) / 2
        val pieY = (width - pieHeight) / 2
        // draw pie chart
        val total = dataSet.sumOf { it.value }.roundToInt()
        var currentValue = 0.0
        dataSet.forEach {
            val startAngle = ceil(currentValue * 360 / total).roundToInt()
            val arcAngle = ceil(it.value * 360 / total).roundToInt()
            g2.color = Colors.getColor(it.color)
            val arc = Arc2D.Double(
                pieX.toDouble(),
                pieY.toDouble(),
                pieWidth.toDouble(),
                pieHeight.toDouble(),
                startAngle.toDouble(),
                arcAngle.toDouble(),
                Arc2D.PIE
            )
            g2.fill(arc)
            if (displayOutline) {
                g2.color = Color.BLACK
                g2.stroke = BasicStroke(2f)
                g2.draw(arc)
            }
            currentValue += it.value
        }
        // draw bottom chart legend
        g2.drawLegend(pieX, pieY)
        // draw title
        title?.takeIf { it.isNotBlank() }?.let {
            g2.drawTextCentered(width, height, it, 24, 0.95)
        }
        // draw subtitle
        subtitle?.takeIf { it.isNotBlank() }?.let {
            g2.drawTextCentered(width, height, it, 12, 0.90)
        }

        return image
    }

    private fun Graphics2D.drawLegend(marginWidth: Int, marginHeight: Int) {
        stroke = BasicStroke(1f)
        val fontSize = 10
        val boxWidth = 12
        val boxHeight = 12
        val padding = 12
        var current = marginWidth - (marginWidth / 2)
        var linePosition = 16
        dataSet.forEach {
            color = Colors.getColor(it.color)
            val rect = Rectangle(current, height - marginHeight + linePosition, boxWidth, boxHeight)
            fill(rect)

            color = Color.BLACK
            font = Font("Serif", Font.PLAIN, fontSize)
            // pie chart displays values as well in the legend
            val text = if (it.label.length > 20)
                "${it.label.substring(0, 19)}... (${String.format("%.2f", it.value)})"
            else "${it.label} (${String.format("%.2f", it.value)})"
            val stringWidth = getFontMetrics(font).stringWidth(text)
            drawString(
                text,
                current + boxWidth + (padding / 2),
                height - marginHeight + linePosition + boxHeight
            )
            current += boxWidth + stringWidth + padding
            if (current >= (width * 0.8).roundToInt()) {
                current = marginWidth - (marginWidth / 2)
                linePosition += 16
            }
        }
    }
}
