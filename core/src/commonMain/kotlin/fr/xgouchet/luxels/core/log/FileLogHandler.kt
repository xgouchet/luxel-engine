package fr.xgouchet.luxels.core.log

import fr.xgouchet.luxels.core.log.LogHandler.Companion.INDENTATIONS
import okio.FileSystem
import okio.Path
import okio.SYSTEM
import okio.buffer
import okio.use

class FileLogHandler(
    val outputLogPath: Path,
    val minLevel: Log.Level
) : LogHandler {

    private var sectionLevel: Int = 0

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

            else -> {
                // ignore
            }
        }
    }

    // endregion

    // region Internal

    private fun append(
        prefix: String,
        message: String
    ) {
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