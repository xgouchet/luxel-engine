package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.luxels.core.log.Log
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.test.stub.core.SimpleCallRecorder
import fr.xgouchet.luxels.core.test.stub.core.SimpleResponseHandler
import fr.xgouchet.luxels.core.test.stub.core.Stub
import fr.xgouchet.luxels.core.test.stub.core.handleUnitCall

class StubLogHandler : LogHandler, Stub {

    override val callRecorder = SimpleCallRecorder()

    override val responseHandler = SimpleResponseHandler()

    private val handledLogs = mutableListOf<Log>()

    // region StubLogHandler

    fun getLogs(): List<Log> {
        return handledLogs
    }

    // endregion

    // region LogHandler

    override fun onLog(log: Log) {
        handledLogs.add(log)
        handleUnitCall("onLog", mapOf("log" to log))
    }

    // endregion
}
