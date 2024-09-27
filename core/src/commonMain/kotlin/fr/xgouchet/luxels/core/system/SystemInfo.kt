package fr.xgouchet.luxels.core.system

/**
 * A class giving access to runtime system information.
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object SystemInfo {

    /**
     * Get the number of thread that can run safely in parallel.
     */
    fun getParallelCapacity(): Int

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
