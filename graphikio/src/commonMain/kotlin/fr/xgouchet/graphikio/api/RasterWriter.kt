package fr.xgouchet.graphikio.api

import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.graphikio.format.ImageFormatConstraints
import okio.Sink

/**
 * A class able to write a [RasterData] image to a [Sink].
 */
interface RasterWriter {
    /** The file extension associated with this writer for the given format. */
    fun fileExtension(constraints: ImageFormatConstraints): String

    /**
     * Checks whether the given format constraints is supported by this writer.
     * @param constraints the constraints to check
     */
    fun supportsFormat(constraints: ImageFormatConstraints): Boolean

    /**
     * Writes the provided data into the given sink.
     * @param rasterData the raster data
     * @param sink the sink
     */
    fun write(rasterData: RasterData, sink: Sink)
}
