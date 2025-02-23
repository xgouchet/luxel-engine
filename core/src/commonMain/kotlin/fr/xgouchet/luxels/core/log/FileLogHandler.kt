package fr.xgouchet.luxels.core.log

import fr.xgouchet.luxels.core.log.LogHandler.Companion.INDENTATIONS
import okio.FileSystem
import okio.Path
import okio.SYSTEM
import okio.buffer
import okio.use

/**
 *
 * A [LogHandler] that prints the logs in the standard output.
 *
 * @property outputLogPath the [Path] to the file where logs are written
 * @property minLevel the minimum Log Level to report
 */
class FileLogHandler(
    val outputLogPath: Path,
    val minLevel: Log.Level = Log.Level.INFO,
) : LogHandler {

    private val fs: FileSystem = FileSystem.SYSTEM

    private var sectionLevel: Int = 0

    init {
        val parentPath = outputLogPath.parent
        requireNotNull(parentPath)
        println("Creating dirs $parentPath")
        fs.createDirectories(parentPath)
    }

    // region LogHandler

    override fun onLog(log: Log) {
        when (log) {
            is Log.Message -> {
                if (log.level >= minLevel) {
                    append(log.level.symbol, log.content)
                }
            }

            is Log.StartSection -> {
                append("-", log.title)
                sectionLevel++
            }

            is Log.EndSection -> {
                sectionLevel--
            }

            is Log.StartProgress,
            is Log.Progress,
            is Log.EndProgress,
            -> {
                // ignore progression
            }
        }
    }

    // endregion

    // region Internal

    private fun append(prefix: String, message: String) {
        val indentation = INDENTATIONS[sectionLevel]
        FileSystem.SYSTEM.appendingSink(outputLogPath, mustExist = false)
            .use { fileSink ->
                fileSink.buffer().use { bufferSink ->
                    bufferSink.writeUtf8("$prefix $indentation $message")
                }
                fileSink.flush()
            }
    }

    // endregion
}
