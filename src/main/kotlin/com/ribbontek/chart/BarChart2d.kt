package com.ribbontek.chart

import com.ribbontek.style.Colors
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.geom.Line2D
import java.awt.image.BufferedImage
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.log10
import kotlin.math.roundToInt

data class BarDataSet(
    val label: String,
    val value: Double,
    val color: String
)

class BarChart2d(
    var width: Int = -1,
    var height: Int = -1,
    var dataSet: List<BarDataSet> = emptyList(),
    var backgroundColor: Color = Color.WHITE,
    var title: String? = null,
    var subtitle: String? = null
) : AbstractChart() {

    override fun render(): BufferedImage {
        // validate bar chart data
        validate()
        // set up image
        val image = setUpImage(width, height, backgroundColor)
        val g2: Graphics2D = image.createGraphics()
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        // draw bar chart
        val barChartHeight = (height * 0.7).roundToInt()
        val barChartWidth = (width * 0.7).roundToInt()
        val marginHeight = (height - barChartHeight) / 2
        val marginWidth = (width - barChartWidth) / 2

        // calculate the highest value
        val maxValue = dataSet.maxOf { it.value }
        val number = "1" + (1 until maxValue.roundToInt().countDigits()).joinToString("") { "0" }
        val highestNumber = ceil((maxValue / number.toDouble())).roundToInt() * number.toInt()

        var current = marginWidth
        val padding = 12
        val totalPadding = 12 * (dataSet.size - 1)
        val barWidth = (barChartWidth - totalPadding) / dataSet.size

        dataSet.forEach {
            g2.color = Colors.getColor(it.color)
            val barHeight = ceil((it.value / highestNumber) * barChartHeight).roundToInt()
            val rect = Rectangle(current, (height - marginHeight) - barHeight, barWidth, barHeight)
            g2.fill(rect)
            current += barWidth + padding
        }
        // draw bar chart measurement lines
        g2.drawXyAxis(marginWidth, marginHeight, barChartWidth, barChartHeight)
        // draw bottom chart legend
        g2.drawLegend(marginWidth, marginHeight)
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
            val text = if (it.label.length > 20)
                it.label.substring(0, 19) + "..."
            else it.label
            val stringWidth = getFontMetrics(font).stringWidth(text)
            drawString(
                text,
                current + boxWidth + (padding / 2),
                height - marginHeight + linePosition + boxHeight
            )
            if (current >= (width * 0.7).roundToInt()) {
                current = marginWidth - (marginWidth / 2)
                linePosition += 16
            } else {
                current += boxWidth + stringWidth + padding
            }
        }
    }

    private fun Graphics2D.drawXyAxis(marginWidth: Int, marginHeight: Int, barChartWidth: Int, barChartHeight: Int) {
        color = Color.BLACK
        stroke = BasicStroke(2f)
        // draw x - horizontal
        draw(
            Line2D.Double(
                marginWidth.toDouble(),
                (marginHeight + barChartHeight).toDouble(),
                (marginWidth + barChartWidth).toDouble(),
                (marginHeight + barChartHeight).toDouble()
            )
        )
        // draw y - vertical
        draw(
            Line2D.Double(
                marginWidth.toDouble(),
                marginHeight.toDouble(),
                marginWidth.toDouble(),
                (marginHeight + barChartHeight).toDouble()
            )
        )
        // draw sidebar lines
        val sideBarDisplay = sideBarDisplay()
        (0..sideBarDisplay.numberOfSegments).forEach { position ->
            // move y position
            stroke = BasicStroke(1f)
            val movement = (barChartHeight / sideBarDisplay.numberOfSegments) * position
            draw(
                Line2D.Double(
                    marginWidth.toDouble(),
                    (marginHeight + barChartHeight).toDouble() - movement,
                    marginWidth.toDouble() - 6,
                    (marginHeight + barChartHeight).toDouble() - movement
                )
            )

            font = Font("Serif", Font.PLAIN, 6)
            val text = "%,d".format(position * sideBarDisplay.segmentStep)
            val fontMetrics = getFontMetrics(font)
            drawString(
                text,
                marginWidth - 10 - fontMetrics.stringWidth(text),
                (marginHeight + barChartHeight - movement) + (fontMetrics.height / 4)
            )
        }
    }

    private fun sideBarDisplay(): SideBarDisplay {
        val maxNumber = dataSet.maxOf { it.value }
        val number = "1" + (1 until maxNumber.roundToInt().countDigits()).joinToString("") { "0" }
        val highestNumber = ceil((maxNumber / number.toDouble())).roundToInt() * number.toInt()
        return SideBarDisplay(
            segmentStep = number.toInt(),
            numberOfSegments = highestNumber / number.toInt()
        )
    }

    data class SideBarDisplay(
        val numberOfSegments: Int,
        val segmentStep: Int
    )

    private fun Int.countDigits() = when (this) {
        0 -> 1
        else -> log10(abs(toDouble())).toInt() + 1
    }

    private fun validate() {
        assert(width > 0) { "Width for ${this::class.simpleName} must be greater than 0" }
        assert(height > 0) { "Height for ${this::class.simpleName} must be greater than 0" }
        assert(dataSet.isNotEmpty()) { "Data Set for ${this::class.simpleName} must not be empty" }
    }
}
