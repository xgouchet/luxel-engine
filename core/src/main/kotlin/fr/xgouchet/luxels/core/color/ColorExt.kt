package fr.xgouchet.luxels.core.color

internal fun java.awt.Color.asColor(): Color {
    return Color(
        red / 255.0,
        green / 255.0,
        blue / 255.0,
        alpha / 255.0,
    )
}
