package fr.xgouchet.luxels.core.color

import fr.xgouchet.graphikio.color.HDRColor

/**
 * An interface providing a color information for a Luxel.
 */
fun interface ColorSource {
    /**
     * @return the current color for the luxel.
     */
    fun color(): HDRColor
}
