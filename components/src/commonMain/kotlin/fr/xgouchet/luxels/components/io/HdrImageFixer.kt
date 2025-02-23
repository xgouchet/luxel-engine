package fr.xgouchet.luxels.components.io

import fr.xgouchet.graphikio.format.hdr.HdrImageFormat
import fr.xgouchet.luxels.core.io.ImageFixer
import okio.Path

/**
 * An [ImageFixer] exporting an exposed film to an HDR (Radiance) image file.
 * @param outputDirPath the path to the output dir where the file should be saved
 */
class HdrImageFixer(
    outputDirPath: Path,
) : AbstractRasterImageFixer(
    HdrImageFormat,
    outputDirPath,
)
