package fr.xgouchet.luxels.core.io

import fr.xgouchet.luxels.core.render.Film

/**
 * Used to convert the exposed film into a digital Image file.
 */
interface ImageFixer {
    /**
     * Write the data from the given film into a file.
     * @param film the [Film] to read from
     * @param outputName the name of the file to write to
     */
    fun write(film: Film, outputName: String)
}
