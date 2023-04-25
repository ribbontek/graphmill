Add the following code to Scatter Chart 2d to test the zero position dot & sample data

```kotlin
// EXAMPLE ZERO POSITION DOT - TEST POSITIONING
color = Color.BLUE
fill(Ellipse2D.Double(xZeroStart, yZeroStart, 3.0, 3.0))
listOf(
    ScatterDataSet.Coordinate(x = 10.0, y = 15.0),
    ScatterDataSet.Coordinate(x = -50.0, y = -20.0)
).forEach { coordinate ->
    val xPos = (coordinate.x / xRange) * distance
    val yPos = (coordinate.y / yRange) * distance
    println("data: $coordinate, xPos: $xPos, yPos: $yPos")

    val ellipse = Ellipse2D.Double(
        xZeroStart + xPos,
        yZeroStart - yPos,
        3.0,
        3.0
    )
    fill(ellipse)
}
```