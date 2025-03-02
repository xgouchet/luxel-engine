package art.luxels.imageio.format.bmp

import art.luxels.imageio.format.ImageFormat
import art.luxels.imageio.format.ImageFormatConstraints

/**
 * The Bitmap Image Format, based on Microsoft's DIB (Device-independent Bitmaps) format.
 *
 * It doesn't support high dynamic range, it doesn't support transparency, and is lossless.
 *
 * It usually is the most accurate and simple lossless format, but the heaviest too.
 *
 * TODO support all types of BMP/DIB header formats
 */
object BmpImageFormat : ImageFormat(
    fileNameExtension = "bmp",
    constraints = ImageFormatConstraints(
        isHDRSupported = false,
        isTransparencySupported = false,
        isLossless = true,
    ),
) {
    internal const val BMP_PREFIX = "BM"

    internal const val BYTE_ALIGN_COUNT = 4
    internal const val BIT_ALIGN_COUNT = 4 * Byte.SIZE_BITS

    internal const val BYTES_PER_PIXEL = 3
    internal const val BITS_PER_PIXEL = BYTES_PER_PIXEL * Byte.SIZE_BITS

    internal const val BMP_HEADER_SIZE = 14
    internal const val DIB_HEADER_SIZE = 40
    internal const val PIXEL_DATA_OFFSET = BMP_HEADER_SIZE + DIB_HEADER_SIZE
    internal const val COLOR_PLANE_COUNT = 1
    internal const val COLOR_PALETTE_SIZE = 0
    internal const val IMPORTANT_COLOR_COUNT = 0

    /** Value for the Compression header field, means no compression. */
    internal const val COMPRESSION_BI_RGB = 0

    /** Print resolution in Pixel per Meter. 72 ppi ~ 2835 ppm. */
    internal const val PRINT_RESOLUTION_PPM = 2835
}
