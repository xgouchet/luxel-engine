package art.luxels.imageio.data

/**
 * The total number of pixels in the raster data.
 */
val RasterData.pixelCount: Int
    get() = width * height
