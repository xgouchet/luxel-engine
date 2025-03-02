package art.luxels.imageio.data

import art.luxels.imageio.color.Color

/**
 * An interface for rasterized image data, allowing edition of the pixel content.
 */
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
