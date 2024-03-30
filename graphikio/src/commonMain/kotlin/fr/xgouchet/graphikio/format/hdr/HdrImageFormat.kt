package fr.xgouchet.graphikio.format.hdr

import fr.xgouchet.graphikio.format.ImageFormat

object HdrImageFormat : ImageFormat(
    boundedColor = false,
    supportsAlpha = false,
    lossless = false,
    fileNameExtension = "hdr",
)
