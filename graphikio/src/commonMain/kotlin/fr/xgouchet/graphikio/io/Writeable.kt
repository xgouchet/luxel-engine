package fr.xgouchet.graphikio.io

import okio.BufferedSink

/**
 * Describe data that can be written to an Okio [BufferedSink].
 */
interface Writeable {

    /**
     * Writes the current instance to the given sink.
     * @param sink the sink to write to
     */
    fun write(sink: BufferedSink)
}
