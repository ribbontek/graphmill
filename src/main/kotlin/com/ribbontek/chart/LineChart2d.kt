package com.ribbontek.chart

import com.ribbontek.style.Colors
import com.ribbontek.util.countDigits
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.image.BufferedImage
import kotlin.math.ceil
import kotlin.math.roundToInt

data class LineDataSet(
    val label: String,
    val values: List<Double>,
    val color: String
)

class LineChart2d(
    var width: Int = -1,
    var height: Int = -1,
    var labels: List<String> = emptyList(),
    var dataSet: List<LineDataSet> = emptyList(),
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
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)
        // draw line chart
        val lineChartHeight = (height * 0.7).roundToInt()
        val lineChartWidth = (width * 0.7).roundToInt()
        val marginHeight = (height - lineChartHeight) / 2
        val marginWidth = (width - lineChartWidth) / 2

        // calculate the highest value
        val maxValue = dataSet.maxOf { it.values.max() }
        val number = "1" + (1 until maxValue.roundToInt().countDigits()).joinToString("") { "0" }
        val highestNumber = ceil((maxValue / number.toDouble())).roundToInt() * number.toInt()

        val padding = 12
        val totalPadding = padding * labels.size
        val barWidth = (lineChartWidth - totalPadding) / labels.size

        dataSet.forEach { line ->
            var current = marginWidth + padding
            line.values.take(labels.size).forEachIndexed { index, data ->
                g2.color = Colors.getColor(line.color)
                val lineHeight = ceil((data / highestNumber) * lineChartHeight).roundToInt()
                if (line.values.size > index + 1 && labels.size > index + 1) {
                    val futureX = current + barWidth + padding
                    val futureY = ceil((line.values[index + 1] / highestNumber) * lineChartHeight).roundToInt()
                    g2.draw(
                        Line2D.Double(
                            current.toDouble(),
                            ((height - marginHeight) - lineHeight).toDouble(),
                            futureX.toDouble(),
                            ((height - marginHeight) - futureY).toDouble()
                        )
                    )
                }
                val ellipse = Ellipse2D.Double(
                    current - 1.5,
                    ((height - marginHeight) - lineHeight) - 1.5,
                    3.0,
                    3.0
                )
                g2.fill(ellipse)
                current += barWidth + padding
            }
        }

        g2.color = Color.BLACK
        g2.stroke = BasicStroke(1f)
        var current = marginWidth + padding
        labels.forEach {
            g2.draw(
                Line2D.Double(
                    current.toDouble(),
                    (height - marginHeight).toDouble(),
                    current.toDouble(),
                    ((height - marginHeight) + 6).toDouble()
                )
            )
            g2.font = Font("Serif", Font.PLAIN, 8)
            val fontMetrics = g2.getFontMetrics(g2.font)
            g2.drawStringRotate(
                it,
                current.toDouble(),
                ((height - marginHeight) + 6 + fontMetrics.height).toDouble(),
                45.0
            )
            current += barWidth + padding
        }

        // draw line chart measurement lines
        g2.drawXyAxis(marginWidth, marginHeight, lineChartWidth, lineChartHeight)

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

    private fun Graphics2D.drawStringRotate(text: String, x: Double, y: Double, angle: Double) {
        translate(x, y)
        rotate(Math.toRadians(angle))
        drawString(text, 0, 0)
        rotate(-Math.toRadians(angle))
        translate(-x, -y)
    }

    private fun Graphics2D.drawXyAxis(marginWidth: Int, marginHeight: Int, lineChartWidth: Int, lineChartHeight: Int) {
        color = Color.BLACK
        stroke = BasicStroke(2f)
        // draw x - horizontal
        draw(
            Line2D.Double(
                marginWidth.toDouble(),
                (marginHeight + lineChartHeight).toDouble(),
                (marginWidth + lineChartWidth).toDouble(),
                (marginHeight + lineChartHeight).toDouble()
            )
        )
        // draw y - vertical
        draw(
            Line2D.Double(
                marginWidth.toDouble(),
                marginHeight.toDouble(),
                marginWidth.toDouble(),
                (marginHeight + lineChartHeight).toDouble()
            )
        )
        // draw sidebar lines
        val sideBarDisplay = sideBarDisplay()
        (0..sideBarDisplay.numberOfSegments).forEach { position ->
            // move y position
            stroke = BasicStroke(1f)
            val movement = (lineChartHeight / sideBarDisplay.numberOfSegments) * position
            draw(
                Line2D.Double(
                    marginWidth.toDouble(),
                    (marginHeight + lineChartHeight).toDouble() - movement,
                    marginWidth.toDouble() - 6,
                    (marginHeight + lineChartHeight).toDouble() - movement
                )
            )

            font = Font("Serif", Font.PLAIN, 6)
            val text = "%,d".format(position * sideBarDisplay.segmentStep)
            val fontMetrics = getFontMetrics(font)
            drawString(
                text,
                marginWidth - 10 - fontMetrics.stringWidth(text),
                (marginHeight + lineChartHeight - movement) + (fontMetrics.height / 4)
            )
        }
    }

    private fun sideBarDisplay(): SideBarDisplay {
        val maxValue = dataSet.maxOf { it.values.max() }
        val number = "1" + (1 until maxValue.roundToInt().countDigits()).joinToString("") { "0" }
        val highestNumber = ceil((maxValue / number.toDouble())).roundToInt() * number.toInt()
        return SideBarDisplay(
            segmentStep = number.toInt(),
            numberOfSegments = highestNumber / number.toInt()
        )
    }

    private data class SideBarDisplay(
        val numberOfSegments: Int,
        val segmentStep: Int
    )

    private fun validate() {
        assert(width > 0) { "Width for ${this::class.simpleName} must be greater than 0" }
        assert(height > 0) { "Height for ${this::class.simpleName} must be greater than 0" }
        assert(dataSet.isNotEmpty()) { "Data Set for ${this::class.simpleName} must not be empty" }
        assert(labels.isNotEmpty()) { "Labels for ${this::class.simpleName} must not be empty" }
    }
}
