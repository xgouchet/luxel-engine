package fr.xgouchet.graphikio.writer

import fr.xgouchet.graphikio.format.ImageFormat

abstract class AbstractRasterWriter(
    vararg supportedFormat: ImageFormat,
) : RasterWriter {

    private val supportedFormats: List<ImageFormat> = supportedFormat.toList()

    override fun supportsFormat(imageFormat: ImageFormat): Boolean {
        return supportedFormats.any { it.matches(imageFormat) }
    }
}
