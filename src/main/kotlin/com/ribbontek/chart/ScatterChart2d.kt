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
    var subtitle: String? = null
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
            stroke = BasicStroke(1f)
            val movement = ceil((scatterChartWidth / segmentDisplay.xNumbers.size + 1.5) * index.toDouble())
            draw(
                Line2D.Double(
                    marginWidth + movement,
                    (marginHeight + scatterChartWidth).toDouble(),
                    marginWidth + movement,
                    (marginHeight + scatterChartWidth) + 6.0,
                )
            )

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
            stroke = BasicStroke(1f)
            val movement = (scatterChartHeight / segmentDisplay.yNumbers.size + 1.5) * index
            draw(
                Line2D.Double(
                    marginWidth.toDouble(),
                    (marginHeight + scatterChartHeight).toDouble() - movement,
                    marginWidth - 6.0,
                    (marginHeight + scatterChartHeight).toDouble() - movement
                )
            )

            font = Font("Serif", Font.PLAIN, 6)
            val text = "%,d".format(data)
            val fontMetrics = getFontMetrics(font)
            drawString(
                text,
                marginWidth - 10 - fontMetrics.stringWidth(text),
                (marginHeight + scatterChartHeight - movement.toInt()) + (fontMetrics.height / 4)
            )
        }
        // EXAMPLE ZERO POSITION DOT
        color = Color.GREEN
        fill(
            Ellipse2D.Double(
                marginWidth.toDouble() - 1.5,
                height - marginHeight.toDouble() - 1.5,
                3.0,
                3.0
            )
        )
        // TODO: FINISH TEST DOT FOR RUNNING THE POSITION CALCULATIONS
        val testCoordinate = ScatterDataSet.Coordinate(x = -20.25, y = -54.67)
        val xRange = segmentDisplay.xNumbers.max() - segmentDisplay.xNumbers.min()
        val yRange = segmentDisplay.yNumbers.max() - segmentDisplay.yNumbers.min()

        val xPos = ceil((abs(testCoordinate.x) / xRange) * scatterChartWidth).roundToInt()
        val yPos = ceil((abs(testCoordinate.y) / yRange) * scatterChartHeight).roundToInt()
        val ellipse = Ellipse2D.Double(
            marginWidth.toDouble() - 1.5 + xPos,
            height - marginHeight.toDouble() - 1.5 - yPos,
            3.0,
            3.0
        )
        fill(ellipse)
        dataSet.forEach { scatterSet ->
            scatterSet.data.forEach {
                // TODO: calculate the x & y position in the graph
                color = Colors.getColor(scatterSet.color)
                println(it)
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
