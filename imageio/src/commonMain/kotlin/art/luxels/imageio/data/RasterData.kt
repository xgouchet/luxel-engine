package art.luxels.imageio.data

import art.luxels.imageio.color.Color

/**
 * A basic interface for rasterized image data, that is an image that can be expressed as a
 * grid of pixels.
 */
interface RasterData {
    /** The width of the image, i.e.: the number of pixel columns. */
    val width: Int

    /** The height of the image, i.e.: the number of pixel rows. */
    val height: Int

    /**
     * Called whenever data is about to be read.
     *
     * This can be used to preprocess the content, if needed.
     */
    fun prepare()

    /**
     * Get the color of a pixel.
     * @param x the column (0-based) index of the pixel
     * @param y the row (0-based) index of the pixel
     */
    fun getColor(x: Int, y: Int): Color

    /**
     * Get the metadata of the image.
     */
    fun getMetadata(): Map<String, MetadataAttribute>
}
