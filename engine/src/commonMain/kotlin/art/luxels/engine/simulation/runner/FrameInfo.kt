package art.luxels.engine.simulation.runner

import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

/**
 * Information about a frame being rendered.
 * @param index the index of the frame in the simulation
 * @param time the time of the frame in the simulation
 * @param progression the progression within the current animation (always in a [0…1] range)
 */
data class FrameInfo(
    val index: Int,
    val time: Duration,
    val progression: Double,
) {
    /**
     * @return the name of the frame
     */
    fun name(): String {
        return index.toString().padStart(FRAME_PADDING, '0')
    }

    // region Any

    override fun toString(): String {
        return "#${name()} ($time / $progression)"
    }

    // endregion

    companion object {
        private const val FRAME_PADDING = 4

        /**
         * Used in place a of NULL FrameInfo.
         */
        val NULL_FRAME = FrameInfo(0, 0.nanoseconds, 0.0)
    }
}
