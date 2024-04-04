package fr.xgouchet.luxels.core.io

import fr.xgouchet.luxels.core.render.exposure.Film

/**
 * An Image Fixer doing nothing.
 */
class NoOpFixer : ImageFixer {
    // region ImageFixer

    override fun write(film: Film, outputName: String) {
    }

    // endregion
}
