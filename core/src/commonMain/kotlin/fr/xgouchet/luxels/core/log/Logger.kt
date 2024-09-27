package fr.xgouchet.luxels.core.log

/**
 * A logger.
 */
@Suppress("TooManyFunctions")
class Logger(
    private var logHandler: LogHandler,
) {

    // region Logger

    /**
     * Log a verbose info.
     * @param message the message to log
     */
    fun verbose(message: String) {
        addLog(Log.Message(Log.Level.VERBOSE, message))
    }

    /**
     * Log a debug info.
     * @param message the message to log
     */
    fun debug(message: String) {
        addLog(Log.Message(Log.Level.DEBUG, message))
    }

    /**
     * Log an info.
     * @param message the message to log
     */
    fun info(message: String) {
        addLog(Log.Message(Log.Level.INFO, message))
    }

    /**
     * Log a warning.
     * @param message the message to log
     */
    fun warning(message: String) {
        addLog(Log.Message(Log.Level.WARNING, message))
    }

    /**
     * Log an error.
     * @param message the message to log
     */
    fun error(message: String) {
        addLog(Log.Message(Log.Level.ERROR, message))
    }

    /**
     * Start a progression.
     */
    fun startProgress() {
        addLog(Log.StartProgress)
    }

    /**
     * Updates the current progression.
     * @param progress the progression (between 0 and 1)
     * @param message an additional message providing details on the operation in progress
     */
    fun progress(progress: Double, message: String) {
        addLog(Log.Progress(progress, message))
    }

    /**
     * End a progression.
     */
    fun endProgress() {
        addLog(Log.EndProgress)
    }

    /**
     * Start a new section.
     * @param title the name of the section
     */
    fun startSection(title: String) {
        addLog(Log.StartSection(title))
    }

    /**
     * End a section.
     */
    fun endSection() {
        addLog(Log.EndSection)
    }

    // endregion

    // region Internal

    private fun addLog(log: Log) {
        logHandler.onLog(log)
    }

    // endregion
}
