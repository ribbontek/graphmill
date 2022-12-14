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
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

data class ScatterDataSet(
    val label: String,
    val data: List<Coordinate>,
    val color: String
) : DataSet {
    data class Coordinate(
        val x: Double,
        val y: Double
    )
}

class ScatterChart2d(
    override var width: Int = -1,
    override var height: Int = -1,
    override var dataSet: List<ScatterDataSet> = emptyList(),
    var backgroundColor: Color = Color.WHITE,
    var title: String? = null,
    var subtitle: String? = null,
    var includeGrid: Boolean = true
) : AbstractChart<ScatterDataSet>() {

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
        // draw scatter chart
        val scatterChartHeight = (height * 0.7).roundToInt()
        val scatterChartWidth = (width * 0.7).roundToInt()
        val marginHeight = (height - scatterChartHeight) / 2
        val marginWidth = (width - scatterChartWidth) / 2

        // draw scatter chart measurement lines
        g2.drawChart(marginWidth, marginHeight, scatterChartWidth, scatterChartHeight)

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

    private fun Graphics2D.drawChart(
        marginWidth: Int,
        marginHeight: Int,
        scatterChartWidth: Int,
        scatterChartHeight: Int
    ) {
        color = Color.BLACK
        stroke = BasicStroke(2f)
        // draw x - horizontal
        draw(
            Line2D.Double(
                marginWidth.toDouble(),
                (marginHeight + scatterChartHeight).toDouble(),
                (marginWidth + scatterChartWidth).toDouble(),
                (marginHeight + scatterChartHeight).toDouble()
            )
        )
        // draw y - vertical
        draw(
            Line2D.Double(
                marginWidth.toDouble(),
                marginHeight.toDouble(),
                marginWidth.toDouble(),
                (marginHeight + scatterChartHeight).toDouble()
            )
        )

        val segmentDisplay = segmentDisplay()
        segmentDisplay.xNumbers.forEachIndexed { index, data ->
            // move x position - horizontal (BOTTOM)
            color = Color.BLACK
            stroke = BasicStroke(1f)
            val movement = (scatterChartWidth / segmentDisplay.xNumbers.size) * (index + 1).toDouble()
            draw(
                Line2D.Double(
                    marginWidth + movement,
                    (marginHeight + scatterChartWidth).toDouble(),
                    marginWidth + movement,
                    (marginHeight + scatterChartWidth) + 6.0,
                )
            )
            if (includeGrid) {
                color = Color.LIGHT_GRAY
                draw(
                    Line2D.Double(
                        marginWidth + movement,
                        marginHeight.toDouble(),
                        marginWidth + movement,
                        (marginHeight + scatterChartHeight).toDouble()
                    )
                )
            }

            color = Color.BLACK
            font = Font("Serif", Font.PLAIN, 6)
            val text = "%,d".format(data)
            val fontMetrics = getFontMetrics(font)
            drawString(
                text,
                (marginWidth + movement.toInt()) - (fontMetrics.stringWidth(text) / 2),
                marginHeight + scatterChartWidth + 12 + (fontMetrics.height / 4)
            )
        }
        segmentDisplay.yNumbers.forEachIndexed { index, data ->
            // move y position - vertical (LEFT)
            color = Color.BLACK
            stroke = BasicStroke(1f)
            val movement = (scatterChartHeight / segmentDisplay.yNumbers.size) * (index + 1).toDouble()
            draw(
                Line2D.Double(
                    marginWidth.toDouble(),
                    (marginHeight + scatterChartHeight).toDouble() - movement,
                    marginWidth - 6.0,
                    (marginHeight + scatterChartHeight).toDouble() - movement
                )
            )
            if (includeGrid) {
                color = Color.LIGHT_GRAY
                draw(
                    Line2D.Double(
                        marginWidth.toDouble(),
                        (marginHeight + scatterChartHeight).toDouble() - movement,
                        (marginWidth + scatterChartWidth).toDouble(),
                        (marginHeight + scatterChartHeight).toDouble() - movement
                    )
                )
            }

            color = Color.BLACK
            font = Font("Serif", Font.PLAIN, 6)
            val text = "%,d".format(data)
            val fontMetrics = getFontMetrics(font)
            drawString(
                text,
                marginWidth - 10 - fontMetrics.stringWidth(text),
                (marginHeight + scatterChartHeight - movement.toInt()) + (fontMetrics.height / 4)
            )
        }

        val xRange = segmentDisplay.xNumbers.max() - segmentDisplay.xNumbers.min()
        val yRange = segmentDisplay.yNumbers.max() - segmentDisplay.yNumbers.min()
        val xPosZeroIndex = segmentDisplay.xNumbers.indexOf(0) + 1
        val yPosZeroIndex = segmentDisplay.yNumbers.indexOf(0) + 1
        val xZeroStart = marginWidth + ((scatterChartWidth / segmentDisplay.xNumbers.size) * xPosZeroIndex)
        val yZeroStart = marginHeight + scatterChartHeight -
            ((scatterChartHeight / segmentDisplay.yNumbers.size) * yPosZeroIndex)

        // EXAMPLE ZERO POSITION DOT
        color = Color.BLUE
        fill(Ellipse2D.Double(xZeroStart.toDouble() - 1.5, yZeroStart.toDouble() - 1.5, 3.0, 3.0))

        // TODO: FIX UP POSITIONING SYSTEM HERE - NOT ACCURATE
        dataSet.forEach { scatterSet ->
            scatterSet.data.filter { it.y > 90 || it.x > 90 }.forEach {
                color = Colors.getColor(scatterSet.color)

                println(it)
                val xPos = if (it.x < 0) floor((it.x / xRange) * scatterChartWidth).roundToInt()
                else ceil((it.x / xRange) * scatterChartWidth).roundToInt()
                val yPos = if (it.y < 0) floor((it.y / yRange) * scatterChartHeight).roundToInt()
                else ceil((it.y / yRange) * scatterChartHeight).roundToInt()
                println("xPos: $xPos yPos: $yPos")

                val ellipse = Ellipse2D.Double(
                    xZeroStart.toDouble() + xPos,
                    yZeroStart.toDouble() - yPos,
                    3.0,
                    3.0
                )
                fill(ellipse)
            }
        }
    }

    private data class SegmentDisplay(
        val xNumbers: List<Int>,
        val yNumbers: List<Int>
    )

    private fun segmentDisplay(): SegmentDisplay {
        val maxXValue = dataSet.maxOf { set -> set.data.maxOf { it.x } }
        val minXValue = dataSet.minOf { set -> set.data.minOf { it.x } }
        val maxYValue = dataSet.maxOf { set -> set.data.maxOf { it.y } }
        val minYValue = dataSet.minOf { set -> set.data.minOf { it.y } }
        return SegmentDisplay(
            xNumbers = retrieveSegments(minXValue, maxXValue),
            yNumbers = retrieveSegments(minYValue, maxYValue),
        )
    }

    private fun retrieveSegments(minValue: Double, maxValue: Double): List<Int> {
        val maxNumber = "1" + (1 until maxValue.roundToInt().countDigits()).joinToString("") { "0" }
        val minNumber = "1" + (1 until minValue.roundToInt().countDigits()).joinToString("") { "0" }
        val min = minOf(maxNumber.toInt(), minNumber.toInt())
        val highestNumber = ceil((maxValue / maxNumber.toDouble())).roundToInt() * maxNumber.toInt()
        val lowestNumber = floor((minValue / minNumber.toDouble())).roundToInt() * minNumber.toInt()
        val numberOfSegments = (highestNumber / min) + abs(lowestNumber / min)
        var current = lowestNumber
        val xNumbers = mutableListOf<Int>()
        (0..numberOfSegments).forEach {
            xNumbers.add(current)
            if (current != highestNumber) current += min
        }
        return xNumbers
    }
}
