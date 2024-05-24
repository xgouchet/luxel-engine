package fr.xgouchet.graphikio

import fr.xgouchet.graphikio.api.RasterReader
import fr.xgouchet.graphikio.api.RasterWriter
import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.graphikio.format.ImageFormat
import fr.xgouchet.graphikio.format.ImageFormatConstraints
import fr.xgouchet.graphikio.format.bmp.BmpImageFormat
import fr.xgouchet.graphikio.format.bmp.BmpRasterReader
import fr.xgouchet.graphikio.format.bmp.BmpRasterWriter
import fr.xgouchet.graphikio.format.hdr.HdrImageFormat
import fr.xgouchet.graphikio.format.hdr.HdrRasterWriter
import okio.Path
import okio.Sink
import okio.Source

/**
 * Utility object used to read/write image data.
 */
object GraphikIO {

    private val readers: List<RasterReader> by lazy {
        // TODO automatic lazy loading
        listOf(
            BmpRasterReader(),
        )
    }

    private val writers: List<RasterWriter> by lazy {
        // TODO automatic lazy loading
        listOf(
            BmpRasterWriter(),
            HdrRasterWriter(),
        )
    }

    /**
     * The list of image formats this library wan write.
     */
    val writeableFormats = writers.flatMap { it.supportedFormats() }.toSet()

    /**
     * The list of image formats this library wan write.
     */
    val readableFormats = readers.flatMap { it.supportedFormats() }.toSet()

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
    fun write(
        rasterData: RasterData,
        constraints: ImageFormatConstraints,
        directoryPath: Path,
        baseName: String,
    ) {
        val writer = writers.firstOrNull { writer -> writer.supportsFormatConstraints(constraints) }
        if (writer == null) {
            throw IllegalArgumentException("No writer found to write the provided image")
        }

        fileSystem.createDirectories(directoryPath)
        val fileExtension = writer.fileExtension(constraints)
        val filePath = directoryPath / "$baseName.$fileExtension"
        val sink = fileSystem.sink(filePath, mustCreate = false)

        writer.write(rasterData, sink)
    }

    /**
     * Writes the provided image data to the provided [Sink].
     * @param rasterData the raster data content
     * @param constraints the desired image format
     * @param sink the sink to write to
     */
    fun write(
        rasterData: RasterData,
        constraints: ImageFormatConstraints,
        sink: Sink,
    ) {
        val writer = writers.firstOrNull { writer -> writer.supportsFormatConstraints(constraints) }
        if (writer == null) {
            throw IllegalArgumentException("No writer found to write the provided image")
        }

        writer.write(rasterData, sink)
    }

    /**
     * Reads the image stored at the given path.
     * @param filePath the path to the image which should be read
     * @return the read [RasterData]
     */
    fun read(filePath: Path): RasterData {
        val fileExtension = filePath.name.substringAfterLast('.')
        val reader = readers.firstOrNull { reader -> reader.supportsFileExtension(fileExtension) }
        if (reader == null) {
            throw IllegalArgumentException("No reader found to read the provided image at $filePath")
        }

        val source = fileSystem.source(filePath)
        return try {
            reader.read(source)
        } catch (e: Exception) {
            throw IllegalStateException("Unable to read image from path $filePath", e)
        }
    }

    fun read(imageFormat: ImageFormat, source: Source): RasterData {
        val reader = readers.firstOrNull { reader -> imageFormat in reader.supportedFormats() }
        if (reader == null) {
            throw IllegalArgumentException("No reader found to read the provided image")
        }
        return try {
            reader.read(source)
        } catch (e: Exception) {
            throw IllegalStateException("Unable to read image from source", e)
        }
    }
}
