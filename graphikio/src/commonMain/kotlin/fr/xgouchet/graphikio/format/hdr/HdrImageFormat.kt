package fr.xgouchet.graphikio.format.hdr

import fr.xgouchet.graphikio.format.ImageFormat
import fr.xgouchet.graphikio.format.ImageFormatConstraints

/**
 * The HDR (Radiance / RGBE) Image Format.
 *
 * It supports high dynamic range, it doesn't support transparency, and is lossless.
 */
object HdrImageFormat : ImageFormat(
    fileNameExtension = "hdr",
    constraints = ImageFormatConstraints(
        isHDRSupported = true,
        isTransparencySupported = false,
        isLossless = true,
    ),
)
