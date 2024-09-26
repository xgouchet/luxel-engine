package fr.xgouchet.luxels.core.log

sealed class Log {

    enum class Level(val symbol: String) {
        VERBOSE("…"),
        DEBUG("⚙"),
        INFO("ℹ"),
        WARNING("⚠"),
        ERROR("☢")
    }

    data class Message(
        val level: Level,
        val content: String
    ) : Log()

    data class Progress(
        val progress: Double,
        val message: String
    ) : Log()

    data object StartProgress : Log()

    data object EndProgress : Log()

    data class StartSection(
        val title: String,
    ) : Log()

    data object EndSection : Log()
}