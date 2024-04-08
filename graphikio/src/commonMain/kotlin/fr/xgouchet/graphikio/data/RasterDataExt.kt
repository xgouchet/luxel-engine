package fr.xgouchet.graphikio.data

/**
 * The total number of pixels in the raster data.
 */
val RasterData.pixelCount: Int
    get() = width * height
