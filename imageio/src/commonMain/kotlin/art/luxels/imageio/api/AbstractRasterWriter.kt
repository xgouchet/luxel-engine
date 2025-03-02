package art.luxels.imageio.api

import art.luxels.imageio.format.ImageFormat
import art.luxels.imageio.format.ImageFormatConstraints

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

    override fun supportsFormatConstraints(constraints: ImageFormatConstraints): Boolean {
        return supportedFormats.any { it.constraints == constraints }
    }

    override fun supportedFormats(): Collection<ImageFormat> {
        return supportedFormats
    }

    // endregion
}
