package fr.xgouchet.luxels.engine.render

import fr.xgouchet.luxels.core.render.CleanFilm
import fr.xgouchet.luxels.core.render.Film
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.core.render.RoughFilm
import fr.xgouchet.luxels.engine.api.configuration.FilmType

/**
 * A default implementation of [FilmProvider].
 */
class DefaultFilmProvider : FilmProvider {

    // region FilmProvider

    override fun createFilm(type: FilmType, resolution: Resolution): Film {
        return when (type) {
            FilmType.CLEAN -> CleanFilm(resolution)
            FilmType.ROUGH -> RoughFilm(resolution)
        }
    }

    // endregion
}
