package fr.xgouchet.luxels.core.io

import fr.xgouchet.graphikio.format.hdr.HdrImageFormat
import okio.Path

class HdrImageFixer(
    outputDirPath: Path,
) : AbstractRasterImageFixer(
    HdrImageFormat,
    outputDirPath,
)
