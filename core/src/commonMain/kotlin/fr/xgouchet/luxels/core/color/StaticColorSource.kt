package fr.xgouchet.luxels.core.color

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.graphikio.color.asHDR

/**
 * A [ColorSource] with a single immutable color.
 * Boring.
 * @param color the color
 */
class StaticColorSource(
    private val color: Color,
) : ColorSource {
    // region ColorSource

    override fun color(): HDRColor {
        return color.asHDR()
    }

    // endregion
}
