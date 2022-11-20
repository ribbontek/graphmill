package com.ribbontek

import java.awt.Color

enum class Colors(val color: Color) {
    WHITE(Color.white),
    LIGHTGRAY(Color.lightGray),
    GRAY(Color.gray),
    DARKGRAY(Color.darkGray),
    BLACK(Color.black),
    RED(Color.red),
    PINK(Color.pink),
    ORANGE(Color.orange),
    YELLOW(Color.yellow),
    GREEN(Color.green),
    MAGENTA(Color.magenta),
    CYAN(Color.cyan),
    BLUE(Color.blue);

    companion object {
        fun getColor(color: String, default: Color = Color.green): Color {
            return Colors.values().find { it.name.equals(color, ignoreCase = true) }?.color ?: default
        }
    }
}
