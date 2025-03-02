package art.luxels.core.io

import art.luxels.core.log.LogHandler
import art.luxels.core.log.warning
import art.luxels.core.render.Film

/**
 * An Image Fixer doing nothing.
 *
 * @property logHandler the [LogHandler] to use when writing an image
 */
class NoOpFixer(val logHandler: LogHandler) : ImageFixer {

    // region ImageFixer

    override fun write(film: Film, outputName: String) {
        logHandler.warning("Using NoOpFixer, can't write to $outputName")
    }

    // endregion
}
