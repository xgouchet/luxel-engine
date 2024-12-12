package fr.xgouchet.luxels.engine.simulation.runner

import kotlin.time.Duration

/**
 * Information about a frame being rendered.
 * @param index the index of the frame in the simulation
 * @param time the time of the frame in the simulation
 */
data class FrameInfo(
    val index: Int,
    val time: Duration,
) {
    /**
     * @return the name of the frame
     */
    fun name(): String {
        return index.toString().padStart(FRAME_PADDING, '0')
    }

    // region Any

    override fun toString(): String {
        return "#${name()} ($time)"
    }

    // endregion

    companion object {
        private const val FRAME_PADDING = 4
    }
}
