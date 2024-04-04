package fr.xgouchet.graphikio.format.bmp

import fr.xgouchet.graphikio.format.ImageFormat
import fr.xgouchet.graphikio.format.ImageFormatConstraints

/**
 * The Bitmap Image Format.
 *
 * It doesn't support high dynamic range, it doesn't support transparency, and is lossless.
 *
 * It usually is the most accurate and simple lossless format, but the heaviest too.
 */
object BmpImageFormat : ImageFormat(
    fileNameExtension = "bmp",
    constraints = ImageFormatConstraints(
        isHDRSupported = false,
        isTransparencySupported = false,
        isLossless = true,
    ),
)
