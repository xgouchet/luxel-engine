package fr.xgouchet.luxels.core.io

import fr.xgouchet.luxels.core.render.exposure.Film

internal class NoOpFixer : ImageFixer {

    override fun write(film: Film, outputName: String) {
    }

}