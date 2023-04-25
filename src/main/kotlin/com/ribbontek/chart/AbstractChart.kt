package com.ribbontek.chart

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.GraphicsConfiguration
import java.awt.GraphicsEnvironment
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt

interface DataSet

interface ChartValidator {
    fun validate()
}

abstract class AbstractChart<T : DataSet> : ChartValidator {

    abstract var width: Int
    abstract var height: Int
    abstract var dataSet: List<T>

    protected abstract fun render(): BufferedImage

    fun renderToByteArray(): ByteArray {
        val baos = ByteArrayOutputStream()
        ImageIO.write(render(), "jpg", baos)
        return baos.toByteArray()
    }

    fun renderToFile(
        directory: String = "",
        fileName: String = System.currentTimeMillis().toString()
    ) {
        render().toFile(directory, fileName)
    }

    fun renderToFileGpuEnhanced(
        directory: String = "",
        fileName: String = System.currentTimeMillis().toString()
    ) {
        render().toFileGpuEnhanced(directory, fileName)
    }

    private fun BufferedImage.toFile(directory: String, fileName: String) {
        val file = File(directory, "/$fileName.jpg")
        file.parentFile.mkdirs()
        file.writeBytes(ByteArray(0))
        ImageIO.write(this, "jpg", file)
    }

    private fun BufferedImage.toFileGpuEnhanced(directory: String, fileName: String) {
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val gc: GraphicsConfiguration = ge.defaultScreenDevice.defaultConfiguration
        val vImage = gc.createCompatibleVolatileImage(width, height)
        val g2 = vImage.createGraphics()
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)
        g2.drawImage(this, null, 0, 0)
        g2.scale(0.1, 0.1)
        vImage.snapshot.toFile(directory, fileName)
    }

    protected fun setUpImage(width: Int, height: Int, backgroundColor: Color): BufferedImage {
        // set up graphics 2d
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g2: Graphics2D = image.createGraphics()
        // set up background
        g2.color = backgroundColor
        g2.fillRect(0, 0, width, height)
        return image
    }

    protected fun Graphics2D.drawTextCentered(
        width: Int,
        height: Int,
        text: String,
        size: Int,
        posY: Double
    ) {
        color = Color.BLACK
        font = Font("Serif", Font.PLAIN, size)
        val centeredWidth = width - (width * 0.5).roundToInt()
        val halfFontWidth = getFontMetrics(font).stringWidth(text) / 2
        drawString(
            text,
            centeredWidth - halfFontWidth,
            height - (height * posY).roundToInt()
        )
    }

    override fun validate() {
        assert(width > 0) { "Width for ${this::class.simpleName} must be greater than 0" }
        assert(height > 0) { "Height for ${this::class.simpleName} must be greater than 0" }
        assert(dataSet.isNotEmpty()) { "Data Set for ${this::class.simpleName} must not be empty" }
    }
}
