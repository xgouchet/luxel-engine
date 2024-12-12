package fr.xgouchet.luxels.core.concurrency

import fr.xgouchet.luxels.core.render.Resolution

/**
 * A utility to gather the capabilities for multi-threaded simulations.
 */
interface ConcurrencyCapabilities {
    /**
     * Get the number of thread that can run safely in parallel based on CPU architecture.
     */
    fun getCpuParallelCapacity(): Int

    /**
     * Get the number of thread that can run safely in parallel based on available memory.
     */
    fun getMemoryParallelCapacity(resolution: Resolution): Int
}
