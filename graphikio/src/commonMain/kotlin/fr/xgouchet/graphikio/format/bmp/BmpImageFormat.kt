package fr.xgouchet.graphikio.format.bmp

import fr.xgouchet.graphikio.format.ImageFormat

object BmpImageFormat : ImageFormat(
    boundedColor = true,
    supportsAlpha = false,
    lossless = true,
    fileNameExtension = "bmp",
)
