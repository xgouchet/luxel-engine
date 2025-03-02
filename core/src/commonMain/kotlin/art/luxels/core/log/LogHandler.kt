package art.luxels.core.log

/**
 * Represents an entity which can handle logs as they come and present them in the desired
 * destination.
 */
interface LogHandler {

    /**
     * Called when a log needs to be handled.
     * @param log the log to handle
     */
    fun onLog(log: Log)

    companion object {
        internal val INDENTATIONS = (0..16).map { CharArray(it * 2) { ' ' }.concatToString() }
    }
}
