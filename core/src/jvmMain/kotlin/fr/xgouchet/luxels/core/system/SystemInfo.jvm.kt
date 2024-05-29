package fr.xgouchet.luxels.core.system


/**
 * A class giving access to runtime system information.
 */
actual object SystemInfo {

    /***
     * @return the number of thread that can run safely in parallel
     */
    actual fun getParallelCapacity(): Int {
        return Runtime.getRuntime().availableProcessors()
    }

    /**
     * @return the global memory size dedicated to the current process
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

    private fun byteToMb(value: Long): Long {
        return value / (1024 * 1024)
    }
}