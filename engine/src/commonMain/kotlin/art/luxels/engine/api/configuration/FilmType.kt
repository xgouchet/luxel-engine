package art.luxels.engine.api.configuration

/**
 * The type of film to use to capture the luxel exposition.
 */
enum class FilmType {
    /** Provides a clean and accurate film (slower). */
    CLEAN,

    /** Provides an approximate film (faster). */
    ROUGH,
}
