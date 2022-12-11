package com.ribbontek

import com.ribbontek.chart.AbstractChart
import org.junit.jupiter.api.Test
import java.awt.Color
import java.awt.Polygon
import java.awt.geom.Line2D
import java.awt.image.BufferedImage

/**
 * Little experiment found here:
 * https://stackoverflow.com/questions/4263103/draw-3d-house-using-java2d
 */
class HouseTest : AbstractChartTest() {

    @Test
    fun `build house`() {
        House().renderToFile(BUILD_DIR)
    }

    class House : AbstractChart() {

        override fun render(): BufferedImage {
            val image = setUpImage(800, 600, Color.black)
            val g2 = image.createGraphics()
            var p = Polygon()
            g2.color = Color(0xAEAEAE)
            p.addPoint(100, 50)
            p.addPoint(300, 50)
            p.addPoint(250, 100)
            p.addPoint(50, 100)
            g2.fillPolygon(p)
            g2.color = Color(0xAEAEAE)
            g2.draw(Line2D.Double(300.0, 50.0, 350.0, 100.0))
            g2.draw(Line2D.Double(350.0, 100.0, 350.0, 180.0))
            g2.draw(Line2D.Double(350.0, 180.0, 250.0, 180.0))
            g2.color = Color(0xFFEABB)
            p = Polygon()
            p.addPoint(50, 100)
            p.addPoint(250, 100)
            p.addPoint(250, 180)
            p.addPoint(50, 180)
            g2.fillPolygon(p)
            g2.color = Color(0xFFFFFF)
            p = Polygon()
            p.addPoint(75, 125)
            p.addPoint(225, 125)
            p.addPoint(225, 150)
            p.addPoint(75, 150)
            g2.fillPolygon(p)
            return image
        }
    }
}
