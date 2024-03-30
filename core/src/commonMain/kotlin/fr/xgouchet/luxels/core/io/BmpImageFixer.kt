package fr.xgouchet.luxels.core.io

import fr.xgouchet.graphikio.format.hdr.HdrImageFormat
import okio.Path

class BmpImageFixer(
    iso: Double,
    outputDirPath: Path,
) : AbstractRasterImageFixer(
    HdrImageFormat,
    outputDirPath,
    iso,
)
