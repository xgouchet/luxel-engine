package fr.xgouchet.luxels.core.color

import java.awt.Color as AwtColor

internal fun AwtColor.asColor(): Color {
    return Color(
        red / 255.0,
        green / 255.0,
        blue / 255.0,
        alpha / 255.0,
    )
}
