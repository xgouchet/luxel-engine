package fr.xgouchet.luxels.core.system

/**
 * Prints the system memory information
 */
actual fun printSystemMemInfo() {
    val runtime = Runtime.getRuntime()

    println("  Processors: ${runtime.availableProcessors()}")
    println(" Free Memory: ${byteToMb(runtime.freeMemory())} Mb")
    println("  Max Memory: ${byteToMb(runtime.maxMemory())} Mb")
    println("Total Memory: ${byteToMb(runtime.totalMemory())} Mb")
}

private fun byteToMb(value: Long): Long {
    return value / (1024 * 1024)
}