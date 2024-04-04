package fr.xgouchet.graphikio.format

/**
 * Describes a specific image format.
 * @property fileNameExtension the default extension for files in this format
 * @property constraints the constraints of this format
 */
abstract class ImageFormat(
    val fileNameExtension: String,
    val constraints: ImageFormatConstraints,
)
