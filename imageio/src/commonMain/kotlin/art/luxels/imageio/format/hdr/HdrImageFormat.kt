package art.luxels.imageio.format.hdr

import art.luxels.imageio.format.ImageFormat
import art.luxels.imageio.format.ImageFormatConstraints

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
