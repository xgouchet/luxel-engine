package fr.xgouchet.graphikio.writer

import fr.xgouchet.graphikio.api.RasterWriter
import fr.xgouchet.graphikio.format.ImageFormat
import fr.xgouchet.graphikio.format.ImageFormatConstraints

/**
 * An abstract implementation of [RasterWriter] to simplify handling the supported
 * image formats.
 * @param supportedFormat the format(s) supported by this writer.
 */
abstract class AbstractRasterWriter(
    vararg supportedFormat: ImageFormat,
) : RasterWriter {

    private val supportedFormats: List<ImageFormat> = supportedFormat.toList()

    init {
        check(supportedFormats.isNotEmpty())
    }

    // region RasterWriter

    override fun fileExtension(constraints: ImageFormatConstraints): String {
        return supportedFormats.first { it.constraints == constraints }.fileNameExtension
    }

    override fun supportsFormat(constraints: ImageFormatConstraints): Boolean {
        return supportedFormats.any { it.constraints == constraints }
    }

    // endregion
}
