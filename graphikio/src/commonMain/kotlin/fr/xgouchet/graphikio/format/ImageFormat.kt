package fr.xgouchet.graphikio.format

abstract class ImageFormat(
    val boundedColor: Boolean,
    val supportsAlpha: Boolean,
    val lossless: Boolean,
    val fileNameExtension: String,
) {
    fun matches(other: ImageFormat): Boolean {
        return boundedColor == other.boundedColor &&
            supportsAlpha == other.supportsAlpha &&
            lossless == other.lossless
    }
}
