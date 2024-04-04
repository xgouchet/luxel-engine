package fr.xgouchet.graphikio

import fr.xgouchet.graphikio.api.RasterWriter
import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.graphikio.format.ImageFormatConstraints
import fr.xgouchet.graphikio.format.bmp.BmpImageFormat
import fr.xgouchet.graphikio.format.bmp.BmpRasterWriter
import fr.xgouchet.graphikio.format.hdr.HdrImageFormat
import fr.xgouchet.graphikio.format.hdr.HdrRasterWriter
import okio.Path
import okio.Sink

/**
 * Utility object used to read/write image data.
 */
object GraphikIO {
    private val writers: List<RasterWriter> by lazy {
        // TODO automatic lazy loading
        listOf(
            BmpRasterWriter(),
            HdrRasterWriter(),
        )
    }

    /**
     * The list of image formats supported by this library.
     */
    val supportedFormats = listOf(BmpImageFormat, HdrImageFormat)

    /**
     * Writes the provided image data to a file at the provided path.
     * @param rasterData the raster data content
     * @param constraints the desired image format
     * @param directoryPath the path to the directory where the image should be written
     * @param baseName the base name of the output image file (without the extension)
     */
    fun write(rasterData: RasterData, constraints: ImageFormatConstraints, directoryPath: Path, baseName: String) {
        val writer = writers.firstOrNull { writer -> writer.supportsFormat(constraints) }
        if (writer == null) {
            throw IllegalArgumentException("No writer found to write the provided image")
        }

        fileSystem.createDirectories(directoryPath)
        val fileExtension = writer.fileExtension(constraints)
        val filePath = directoryPath.div("$baseName.$fileExtension")
        val sink = fileSystem.sink(filePath, mustCreate = false)

        writer.write(rasterData, sink)
    }

    /**
     * Writes the provided image data to the provided [Sink].
     * @param rasterData the raster data content
     * @param constraints the desired image format
     * @param sink the sink to write to
     */
    fun write(rasterData: RasterData, constraints: ImageFormatConstraints, sink: Sink) {
        val writer = writers.firstOrNull { writer -> writer.supportsFormat(constraints) }
        if (writer == null) {
            throw IllegalArgumentException("No writer found to write the provided image")
        }

        writer.write(rasterData, sink)
    }
}
