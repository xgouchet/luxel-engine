package art.luxels.cli.common

import art.luxels.components.io.BmpImageFixer
import art.luxels.components.io.HdrImageFixer
import art.luxels.core.io.ImageFixer
import art.luxels.core.log.LogHandler
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

    data object BMP : FixerType() {
        // region FixerType

        override fun createFixer(outputPath: Path, logHandler: LogHandler): ImageFixer {
            return BmpImageFixer(outputPath)
        }

        // endregion
    }
}
