package fr.xgouchet.luxels.core.log

class Logger(
    private var logHandler: LogHandler
) {

    // region Logger

    fun addLog(log: Log) {
        logHandler.onLog(log)
    }

    fun verbose(message: String) {
        addLog(Log.Message(Log.Level.VERBOSE, message))
    }

    fun debug(message: String) {
        addLog(Log.Message(Log.Level.DEBUG, message))
    }

    fun info(message: String) {
        addLog(Log.Message(Log.Level.INFO, message))
    }

    fun warning(message: String) {
        addLog(Log.Message(Log.Level.WARNING, message))
    }

    fun error(message: String) {
        addLog(Log.Message(Log.Level.ERROR, message))
    }


    fun startProgress() {
        addLog(Log.StartProgress)
    }

    fun progress(progress: Double, message: String) {
        addLog(Log.Progress(progress, message))
    }

    fun endProgress() {
        addLog(Log.EndProgress)
    }

    fun startSection(title: String) {
        addLog(Log.StartSection(title))
    }

    fun endSection() {
        addLog(Log.EndSection)
    }

    // endregion
}