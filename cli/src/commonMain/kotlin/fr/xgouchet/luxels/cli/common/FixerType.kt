package fr.xgouchet.luxels.cli.common

import fr.xgouchet.luxels.components.io.BmpImageFixer
import fr.xgouchet.luxels.components.io.HdrImageFixer
import fr.xgouchet.luxels.core.io.ImageFixer
import fr.xgouchet.luxels.core.log.LogHandler
import okio.Path

sealed class FixerType {

    abstract fun createFixer(outputPath: Path, logHandler: LogHandler): ImageFixer

    data object HDR : FixerType() {
        // region FixerType

        override fun createFixer(outputPath: Path, logHandler: LogHandler): ImageFixer {
            return HdrImageFixer(outputPath)
        }

        // endregion
    }

    data class BMP(
        val iso: Double,
    ) : FixerType() {
        // region FixerType

        override fun createFixer(outputPath: Path, logHandler: LogHandler): ImageFixer {
            return BmpImageFixer(outputPath, iso)
        }

        // endregion
    }
}
