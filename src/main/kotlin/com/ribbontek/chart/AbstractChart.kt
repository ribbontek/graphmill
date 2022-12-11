package com.ribbontek.chart

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt

abstract class AbstractChart {

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

    private fun BufferedImage.toFile(directory: String, fileName: String) {
        val file = File(directory, "/$fileName.jpg")
        file.parentFile.mkdirs()
        file.writeBytes(ByteArray(0))
        ImageIO.write(this, "jpg", file)
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

    protected fun drawTextCentered(
        g2: Graphics2D,
        width: Int,
        height: Int,
        text: String,
        size: Int,
        posY: Double
    ) {
        g2.color = Color.BLACK
        g2.font = Font("Serif", Font.PLAIN, size)
        val centeredWidth = width - (width * 0.5).roundToInt()
        val halfFontWidth = g2.getFontMetrics(g2.font).stringWidth(text) / 2
        g2.drawString(
            text,
            centeredWidth - halfFontWidth,
            height - (height * posY).roundToInt()
        )
    }
}
