package fr.xgouchet.luxels.core.log

interface LogHandler {

    fun onLog(log: Log)

    companion object {
        internal val INDENTATIONS = (0..16).map { CharArray(it) { ' ' }.concatToString() }
    }
}