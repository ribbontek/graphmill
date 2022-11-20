package com.ribbontek

import com.ribbontek.style.Colors
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.geom.Line2D
import java.awt.image.BufferedImage
import kotlin.math.ceil
import kotlin.math.roundToInt

data class BarDataSet(
    val label: String,
    val value: Double,
    val color: String
)

fun barChart2d(instance: BarChart2d.() -> Unit): BarChart2d {
    return BarChart2d().apply { instance(this) }
}

enum class DataSetRepresentation {
    TENS, HUNDREDS, THOUSANDS, TENS_OF_THOUSANDS
}

class BarChart2d : AbstractChart() {
    var width: Int = -1
    var height: Int = -1
    var dataSet: List<BarDataSet> = emptyList()
    var backgroundColor: Color = Color.WHITE
    var title: String? = null
    var subtitle: String? = null

    override fun render(): BufferedImage {
        // validate bar chart data
        validate()
        // set up image
        val image = setUpImage(width, height, backgroundColor)
        val g2: Graphics2D = image.createGraphics()
        // draw bar chart
        val barChartHeight = (height * 0.7).roundToInt()
        val barChartWidth = (width * 0.7).roundToInt()
        val marginHeight = (height - barChartHeight) / 2
        val marginWidth = (width - barChartWidth) / 2
        val maxValue = dataSet.maxOf { it.value }
        var current = marginWidth
        val padding = 12
        val totalPadding = 12 * (dataSet.size - 1)
        val barWidth = (barChartWidth - totalPadding) / dataSet.size

        dataSet.forEach {
            g2.color = Colors.getColor(it.color)
            val barHeight = ceil((it.value / maxValue) * barChartHeight).roundToInt()
            val rect = Rectangle(current, (height - marginHeight) - barHeight, barWidth, barHeight)
            g2.fill(rect)
            current += barWidth + padding
        }
        // draw bar chart measurement lines
        g2.color = Color.BLACK
        g2.stroke = BasicStroke(2f)
        g2.draw(
            Line2D.Double(
                marginWidth.toDouble(),
                marginHeight.toDouble(),
                marginWidth.toDouble(),
                (marginHeight + barChartHeight).toDouble()
            )
        )
        g2.draw(
            Line2D.Double(
                marginWidth.toDouble(),
                (marginHeight + barChartHeight).toDouble(),
                (marginWidth + barChartWidth).toDouble(),
                (marginHeight + barChartHeight).toDouble()
            )
        )

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

    // TODO: Create a custom implementation that can calculate the difference between each of the numbers &
    //  create a DataSetRepresentation mapping best fitting for the graph
//    fun averageDistance(dataset: List<BarDataSet>) {
//        val sortedDataset = dataset.map { it.value }.sorted()
//    }

//    public inline fun <S, T : S> Iterable<T>.reduce(operation: (acc: S, T) -> S): S {
//        val iterator = this.iterator()
//        if (!iterator.hasNext()) throw UnsupportedOperationException("Empty collection can't be reduced.")
//        var accumulator: S = iterator.next()
//        while (iterator.hasNext()) {
//            accumulator = operation(accumulator, iterator.next())
//        }
//        return accumulator
//    }

    private fun validate() {
        assert(width > 0) { "Width for ${this::class.simpleName} must be greater than 0" }
        assert(height > 0) { "Height for ${this::class.simpleName} must be greater than 0" }
        assert(dataSet.isNotEmpty()) { "Data Set for ${this::class.simpleName} must not be empty" }
    }
}
