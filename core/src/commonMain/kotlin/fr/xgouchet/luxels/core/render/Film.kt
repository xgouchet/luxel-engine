package fr.xgouchet.luxels.core.render

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.math.Dimension

/**
 * A Film which can be exposed to luxels, to render a simulation.
 */
interface Film : Exposure<Dimension.D2> {
    /** The width of the output image. */
    val width: Int

    /** The height of the output image. */
    val height: Int

    /**
     * Gets the color of a pixel.
     *
     * The returned color is in High Dynamic Range space, and not bound to a specific range.
     *
     * @param i the column index of the pixel
     * @param j the row index of the pixel
     * @return the color of the pixel
     */
    fun getColor(i: Int, j: Int): HDRColor

    /**
     * @return whether this film has actual data or not
     */
    fun hasData(): Boolean
}
