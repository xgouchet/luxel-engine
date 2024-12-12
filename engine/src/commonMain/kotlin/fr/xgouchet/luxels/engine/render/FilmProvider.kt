package fr.xgouchet.luxels.engine.render

import fr.xgouchet.luxels.core.render.Film
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.FilmType

/**
 * A provider creating a Film based on the given configuration.
 */
interface FilmProvider {

    /**
     * Creates a film based on type and resolution.
     * @param type the type of Film
     * @param resolution the resolution
     */
    fun createFilm(type: FilmType, resolution: Resolution): Film
}
