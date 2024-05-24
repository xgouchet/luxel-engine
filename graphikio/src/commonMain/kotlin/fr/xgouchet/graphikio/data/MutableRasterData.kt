package fr.xgouchet.graphikio.data

import fr.xgouchet.graphikio.color.Color

interface MutableRasterData : RasterData {
    /**
     * Set the color of a pixel.
     * @param x the column (0-based) index of the pixel
     * @param y the row (0-based) index of the pixel
     * @param c the color to set
     */
    fun setColor(
        x: Int,
        y: Int,
        c: Color,
    )
}
