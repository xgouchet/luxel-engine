package fr.xgouchet.luxels.core.render.exposure

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.math.geometry.Vector2

/**
 * A Film which can be exposed to luxels, to render a simulation.
 */
interface Film {
    /** The width of the output image. */
    val width: Int

    /** The height of the output image. */
    val height: Int

    /**
     * Exposes color on the film at the given position.
     * @param position the position on the film (should be in the [0…width]⨉[0…height] range,
     * otherwise no color will be exposed)
     * @param color the color to expose.
     */
    fun expose(position: Vector2, color: Color)

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
}
