@file:Suppress("MatchingDeclarationName")

package fr.xgouchet.luxels.core.system

actual object SystemInfo {
    /***
     * @return the number of thread that can run safely in parallel
     */
    actual fun getParallelCapacity(): Int {
        TODO("Not yet implemented")
    }

    /**
     * @return the free memory size remaining for the current process
     */
    actual fun getFreeMemory(): Long {
        TODO("Not yet implemented")
    }

    /**
     * @return the global memory size dedicated to the current process
     */
    actual fun getAvailableMemory(): Long {
        TODO("Not yet implemented")
    }

    /**
     * Prints the system information.
     */
    actual fun printSystemInfo() {}

    /**
     * Trigger a sanitization of the used memory.
     */
    actual fun clearMemory() {}
}
