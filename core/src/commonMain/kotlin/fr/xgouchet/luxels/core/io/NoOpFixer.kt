package fr.xgouchet.luxels.core.io

import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.log.warning
import fr.xgouchet.luxels.core.render.Film

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
