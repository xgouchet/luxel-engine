package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.luxels.core.log.Log
import fr.xgouchet.luxels.core.log.LogHandler

// TODO replace with mocks
class StubLogHandler : LogHandler {

    private val handledLogs = mutableListOf<Log>()

    // region StubLogHandler

    fun getLogs(): List<Log> {
        return handledLogs
    }

    // endregion

    // region LogHandler

    override fun onLog(log: Log) {
        handledLogs.add(log)
    }

    // endregion
}
