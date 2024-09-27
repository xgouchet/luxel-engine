package fr.xgouchet.luxels.core.log

import fr.xgouchet.luxels.core.log.LogHandler.Companion.INDENTATIONS
import kotlin.math.roundToInt

/**
 * A [LogHandler] that prints the logs in the standard output.
 */
class StdOutLogHandler : LogHandler {

    private var sectionLevel: Int = 0

    private val backlog = mutableListOf<Log>()

    private var isShowingProgress: Boolean = false

    // region LogHandler

    override fun onLog(log: Log) {
        if (isShowingProgress) {
            when (log) {
                is Log.EndProgress -> {
                    isShowingProgress = false
                    handleBacklog()
                }

                is Log.Progress -> {
                    handleProgress(log)
                }

                else -> {
                    backlog.add(log)
                }
            }
        } else {
            when (log) {
                is Log.StartProgress -> isShowingProgress = true

                is Log.StartSection -> {
                    printLine("-", log.title)
                    sectionLevel++
                }

                is Log.EndSection -> sectionLevel--

                is Log.Message -> printLine(log.level.symbol, log.content)

                else -> {
                    // No Op
                }
            }
        }
    }

    // endregion

    // region Internal

    private fun handleProgress(progress: Log.Progress) {
        val perThousand = progress.progress * 1000.0
        print("\r… ${perThousand.roundToInt()}‰ ${progress.message}")
    }

    private fun printLine(prefix: String, message: String) {
        val indentation = INDENTATIONS[sectionLevel]
        println("\r$prefix $indentation $message")
    }

    private fun handleBacklog() {
        val copy = backlog.toMutableList()
        backlog.clear()
        copy.forEach { onLog(it) }
    }

    // endregion
}
