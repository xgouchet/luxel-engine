package art.luxels.core.color

import art.luxels.imageio.color.Color
import art.luxels.imageio.color.HDRColor
import art.luxels.imageio.color.asHDR

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
