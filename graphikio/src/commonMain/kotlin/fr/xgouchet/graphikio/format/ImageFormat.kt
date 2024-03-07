package fr.xgouchet.graphikio.format

abstract class ImageFormat(
    val boundedColor: Boolean,
    val fileNameExtension: String,
) {
    fun matches(other: ImageFormat): Boolean {
        return boundedColor == other.boundedColor
    }
}
