package fr.xgouchet.luxels.core.system

import fr.xgouchet.luxels.core.concurrency.ConcurrencyCapabilities
import fr.xgouchet.luxels.core.render.Resolution

/**
 * A class giving access to runtime system information.
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object SystemInfo : ConcurrencyCapabilities {

    // region ConcurrencyCapabilities

    /**
     * Get the number of thread that can run safely in parallel.
     */
    override fun getCpuParallelCapacity(): Int

    /**
     * Get the number of thread that can run safely in parallel based on available memory.
     * @param resolution the expected resolution of the output
     */
    override fun getMemoryParallelCapacity(resolution: Resolution): Int

    // endregion

    /**
     * Get the free memory size remaining for the current process.
     */
    fun getFreeMemory(): Long

    /**
     * Get the global memory size dedicated to the current process.
     */
    fun getAvailableMemory(): Long

    /**
     * Prints the system information.
     */
    fun printSystemInfo()

    /**
     * Trigger a sanitization of the used memory.
     */
    fun clearMemory()
}
