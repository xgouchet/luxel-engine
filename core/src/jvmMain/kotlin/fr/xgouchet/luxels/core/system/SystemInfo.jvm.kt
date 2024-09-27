@file:Suppress("MatchingDeclarationName")

package fr.xgouchet.luxels.core.system

/**
 * A class giving access to runtime system information.
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object SystemInfo {

    /***
     * Get the number of thread that can run safely in parallel.
     */
    actual fun getParallelCapacity(): Int {
        return Runtime.getRuntime().availableProcessors()
    }

    /**
     * Get the free memory size remaining for the current process.
     */
    actual fun getFreeMemory(): Long {
        return Runtime.getRuntime().freeMemory()
    }

    /**
     * Get the global memory size dedicated to the current process.
     */
    actual fun getAvailableMemory(): Long {
        return Runtime.getRuntime().maxMemory()
    }

    /**
     * Prints the system information.
     */
    actual fun printSystemInfo() {
        val runtime = Runtime.getRuntime()

        println("  Processors: ${runtime.availableProcessors()}")
        println(" Free Memory: ${byteToMb(runtime.freeMemory())} Mb")
        println("  Max Memory: ${byteToMb(runtime.maxMemory())} Mb")
        println("Total Memory: ${byteToMb(runtime.totalMemory())} Mb")
    }

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
