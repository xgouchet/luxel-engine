package fr.xgouchet.luxels.core.log

/**
 * Log a verbose info.
 * @param message the message to log
 */
fun LogHandler.verbose(message: String) {
    onLog(Log.Message(Log.Level.VERBOSE, message))
}

/**
 * Log a debug info.
 * @param message the message to log
 */
fun LogHandler.debug(message: String) {
    onLog(Log.Message(Log.Level.DEBUG, message))
}

/**
 * Log an info.
 * @param message the message to log
 */
fun LogHandler.info(message: String) {
    onLog(Log.Message(Log.Level.INFO, message))
}

/**
 * Log a warning.
 * @param message the message to log
 */
fun LogHandler.warning(message: String) {
    onLog(Log.Message(Log.Level.WARNING, message))
}

/**
 * Log an error.
 * @param message the message to log
 */
fun LogHandler.error(message: String) {
    onLog(Log.Message(Log.Level.ERROR, message))
}

/**
 * Start a progression.
 */
fun LogHandler.startProgress() {
    onLog(Log.StartProgress)
}

/**
 * Updates the current progression.
 * @param progress the progression (between 0 and 1)
 * @param message an additional message providing details on the operation in progress
 */
fun LogHandler.progress(progress: Double, message: String) {
    onLog(Log.Progress(progress, message))
}

/**
 * End a progression.
 */
fun LogHandler.endProgress() {
    onLog(Log.EndProgress)
}

/**
 * Start a new section.
 * @param title the name of the section
 */
fun LogHandler.startSection(title: String) {
    onLog(Log.StartSection(title))
}

/**
 * End a section.
 */
fun LogHandler.endSection() {
    onLog(Log.EndSection)
}
