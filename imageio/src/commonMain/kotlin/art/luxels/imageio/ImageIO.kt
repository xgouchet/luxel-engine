package art.luxels.imageio

import art.luxels.imageio.api.RasterReader
import art.luxels.imageio.api.RasterWriter
import art.luxels.imageio.data.RasterData
import art.luxels.imageio.format.ImageFormat
import art.luxels.imageio.format.ImageFormatConstraints
import art.luxels.imageio.format.bmp.BmpImageFormat
import art.luxels.imageio.format.bmp.BmpRasterReader
import art.luxels.imageio.format.bmp.BmpRasterWriter
import art.luxels.imageio.format.hdr.HdrImageFormat
import art.luxels.imageio.format.hdr.HdrRasterWriter
import okio.IOException
import okio.Path
import okio.Sink
import okio.Source

/**
 * Utility object used to read/write image data.
 */
object ImageIO {

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
    @Suppress("TooGenericExceptionCaught")
    fun read(filePath: Path): RasterData {
        val fileExtension = filePath.name.substringAfterLast('.')
        val reader = readers.firstOrNull { reader -> reader.supportsFileExtension(fileExtension) }
        if (reader == null) {
            throw IllegalArgumentException("No reader found to read the provided image at $filePath")
        }

        val source = fileSystem.source(filePath)
        return try {
            reader.read(source)
        } catch (e: IOException) {
            throw IllegalStateException("Unable to read image from path $filePath", e)
        } catch (e: RuntimeException) {
            throw IllegalStateException("Unable to read image from path $filePath", e)
        }
    }

    /**
     * Reads the image from the given source.
     * @param imageFormat the format to read the image in
     * @param source the source input stream
     * @return the read [RasterData]
     */
    @Suppress("TooGenericExceptionCaught")
    fun read(imageFormat: ImageFormat, source: Source): RasterData {
        val reader = readers.firstOrNull { reader -> imageFormat in reader.supportedFormats() }
        if (reader == null) {
            throw IllegalArgumentException("No reader found to read the provided image")
        }
        return try {
            reader.read(source)
        } catch (e: IOException) {
            throw IllegalStateException("Unable to read image from source", e)
        } catch (e: RuntimeException) {
            throw IllegalStateException("Unable to read image from source", e)
        }
    }
}
