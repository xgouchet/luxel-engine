package art.luxels.core.log

/**
 * Represents a log. Duh.
 */
sealed class Log {

    /**
     * The level of visibility for the log.
     * @param symbol the symbol representing the level
     */
    enum class Level(val symbol: String) {
        VERBOSE("…"),
        DEBUG("⚙"),
        INFO("ℹ"),
        WARNING("⚠"),
        ERROR("☢"),
    }

    /**
     * A Message Log.
     * @param level the visibility level
     * @param content the content of the message
     */
    data class Message(
        val level: Level,
        val content: String,
    ) : Log()

    /**
     * A progression log.
     * @param progress the indicator of the progression (between 0 and 1)
     * @param message the message accompanying the progress
     */
    data class Progress(
        val progress: Double,
        val message: String,
    ) : Log()

    /**
     * A message-less log indicating the beginning of a progression.
     */
    data object StartProgress : Log()

    /**
     * A message-less log indicating the end of a progression.
     */
    data object EndProgress : Log()

    /**
     * Indicates the beginning of a new section.
     * @param title the name of the section
     */
    data class StartSection(
        val title: String,
    ) : Log()

    /**
     *
     */
    data object EndSection : Log()
}
