package art.luxels.core.color

import art.luxels.imageio.color.HDRColor

/**
 * An interface providing a color information for a Luxel.
 */
fun interface ColorSource {
    /**
     * @return the current color for the luxel.
     */
    fun color(): HDRColor
}
