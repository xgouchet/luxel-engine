package fr.xgouchet.graphikio.api

import fr.xgouchet.graphikio.format.ImageFormat

/**
 * An abstract implementation of [RasterReader] to simplify handling the supported
 * image formats.
 * @param supportedFormat the format(s) supported by this reader.
 */
abstract class AbstractRasterReader(
    vararg supportedFormat: ImageFormat,
) : RasterReader {

    private val supportedFormats: List<ImageFormat> = supportedFormat.toList()

    init {
        check(supportedFormats.isNotEmpty())
    }

    // region RasterReader

    override fun supportsFileExtension(fileExtension: String): Boolean {
        return supportedFormats.any { it.fileNameExtension == fileExtension }
    }

    override fun supportedFormats(): Collection<ImageFormat> {
        return supportedFormats
    }

    // endregion
}
