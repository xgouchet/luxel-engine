package art.luxels.imageio.api

import art.luxels.imageio.data.RasterData
import art.luxels.imageio.format.ImageFormat
import okio.Source

/**
 * A class able to write a [RasterData] image from a [Source].
 */
interface RasterReader {

    /**
     * The exact image formats supported.
     */
    fun supportedFormats(): Collection<ImageFormat>

    /**
     * Checks whether the given file extension is supported by this reader.
     * @param fileExtension the fileExtension (without the dot character)
     */
    fun supportsFileExtension(fileExtension: String): Boolean

    /**
     * Reads raster data from the given source.
     * @return the raster data
     * @param source the source
     */
    fun read(source: Source): RasterData
}
