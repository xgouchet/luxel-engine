package fr.xgouchet.luxels.core.configuration

import fr.xgouchet.luxels.core.render.exposure.CleanFilm
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.render.exposure.RoughFilm

/**
 * The type of film to use to capture the luxel exposition.
 */
enum class FilmType {

    /** Provides a clean and accurate film (slower). */
    CLEAN,

    /** Provides an approximate film (faster). */
    ROUGH,
    ;

    // region Internal

    internal fun create(resolution: Resolution): Film {
        return when (this) {
            CLEAN -> CleanFilm(resolution)
            ROUGH -> RoughFilm(resolution)
        }
    }

    // endregion
}
