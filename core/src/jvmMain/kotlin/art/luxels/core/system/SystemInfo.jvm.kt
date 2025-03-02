@file:Suppress("MatchingDeclarationName")

package art.luxels.core.system

import art.luxels.core.concurrency.ConcurrencyCapabilities
import art.luxels.core.render.Resolution
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * A class giving access to runtime system information.
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object SystemInfo : ConcurrencyCapabilities {

    // region ConcurrencyCapabilities

    /***
     * Get the number of thread that can run safely in parallel.
     */
    actual override fun getCpuParallelCapacity(): Int {
        return Runtime.getRuntime().availableProcessors()
    }

    /**
     * Get the number of thread that can run safely in parallel based on available memory.
     */
    actual override fun getMemoryParallelCapacity(resolution: Resolution): Int {
        val availableMemory = Runtime.getRuntime().maxMemory()
        val requiredSizePerThread = resolution.pixelCount * Double.SIZE_BYTES * 8
        return floor(availableMemory.toDouble() / (requiredSizePerThread + 1)).roundToInt()
    }

    // endregion

    /**
     * Triggers the Garbage Collection if possible.
     */
    actual fun clearMemory() {
        @Suppress("ExplicitGarbageCollectionCall")
        System.gc()
    }

    // region Internal

    private fun byteToMb(value: Long): Long {
        return value / (1024 * 1024)
    }

    // endregion
}
