package fr.xgouchet.luxels.components.io

import fr.xgouchet.graphikio.format.bmp.BmpImageFormat
import fr.xgouchet.luxels.core.io.ImageFixer
import okio.Path

/**
 * An [ImageFixer] exporting an exposed film to a bitmap BMP image file.
 * @param outputDirPath the path to the output dir where the file should be saved
 * @param iso the sensitivity of the film
 */
class BmpImageFixer(
    outputDirPath: Path,
    iso: Double,
) : AbstractRasterImageFixer(
    BmpImageFormat,
    outputDirPath,
    iso,
)
