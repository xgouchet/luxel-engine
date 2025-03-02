package art.luxels.components.io

import art.luxels.core.io.ImageFixer
import art.luxels.imageio.format.hdr.HdrImageFormat
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
