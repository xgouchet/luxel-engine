package art.luxels.core.test.stub

import art.luxels.core.log.Log
import art.luxels.core.log.LogHandler

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
