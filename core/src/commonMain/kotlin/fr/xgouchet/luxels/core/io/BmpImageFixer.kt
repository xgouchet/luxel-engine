package fr.xgouchet.luxels.core.io

import fr.xgouchet.graphikio.format.bmp.BmpImageFormat
import fr.xgouchet.graphikio.format.hdr.HdrImageFormat
import okio.Path

/**
 * An [ImageFixer] exporting an exposed film to a bitmap BMP image file.
 * @param outputDirPath the path to the output dir where the file should be saved
 * @param iso the sensitivity of the film (can be useful when exporting to SDR formats)
 */
class BmpImageFixer(
    outputDirPath: Path,
    iso: Double,
) : AbstractRasterImageFixer(
    BmpImageFormat,
    outputDirPath,
    iso,
)
