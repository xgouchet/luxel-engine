package art.luxels.engine.render

import art.luxels.core.render.CleanFilm
import art.luxels.core.render.Film
import art.luxels.core.render.Resolution
import art.luxels.core.render.RoughFilm
import art.luxels.engine.api.configuration.FilmType

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
