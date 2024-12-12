@file:Suppress("MatchingDeclarationName")

package fr.xgouchet.luxels.core.system

import fr.xgouchet.luxels.core.concurrency.ConcurrencyCapabilities
import fr.xgouchet.luxels.core.render.Resolution
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
