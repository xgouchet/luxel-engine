package fr.xgouchet.graphikio.format

/**
 * The constraints of an Image Format.
 *
 * @property isHDRSupported whether high dynamic range is supported
 * @property isTransparencySupported whether alpha channel (transparency) is supported
 * @property isLossless whether the format is lossless
 */
data class ImageFormatConstraints(
    val isHDRSupported: Boolean,
    val isTransparencySupported: Boolean,
    val isLossless: Boolean,
)
