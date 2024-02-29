package fr.xgouchet.luxels.core.color

/**
 * An interface providing a color information for a Luxel.
 */
fun interface ColorSource {
    /**
     * @return the current color for the luxel.
     */
    fun color(): Color
}
