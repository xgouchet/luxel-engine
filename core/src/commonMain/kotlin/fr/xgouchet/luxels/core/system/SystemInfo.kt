package fr.xgouchet.luxels.core.system

/**
 * A class giving access to runtime system information.
 */
expect object SystemInfo {

    /***
     * @return the number of thread that can run safely in parallel
     */
    fun getParallelCapacity(): Int

    /**
     * @return the free memory size remaining for the current process
     */
    fun getFreeMemory(): Long

    /**
     * @return the global memory size dedicated to the current process
     */
    fun getAvailableMemory(): Long

    /**
     * Prints the system information.
     */
    fun printSystemInfo()

    /**
     * Triggers the Garbage Collection if any.
     */
    fun gc()
}