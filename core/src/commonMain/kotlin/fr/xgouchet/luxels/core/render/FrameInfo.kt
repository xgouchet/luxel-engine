package fr.xgouchet.luxels.core.render

import kotlin.time.Duration

internal class FrameInfo(
    val frameIndex: Int,
    val frameTime: Duration,
) {
    fun frameName(): String {
        return frameIndex.toString().padStart(FRAME_PADDING, '0')
    }

    // region Any

    override fun toString(): String {
        return "#${frameName()} ($frameTime)"
    }

    // endregion

    companion object {
        const val FRAME_PADDING = 4
    }
}
