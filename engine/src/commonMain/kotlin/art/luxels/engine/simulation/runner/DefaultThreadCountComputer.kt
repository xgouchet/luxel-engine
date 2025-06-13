package art.luxels.engine.simulation.runner

import art.luxels.core.concurrency.ConcurrencyCapabilities
import art.luxels.core.system.SystemInfo
import art.luxels.engine.simulation.CommonConfiguration

/**
 * Default implementation that the following constraints into account.
 * @param concurrencyCapabilities provider to know the capabilities of the current runtime
 */
class DefaultThreadCountComputer(val concurrencyCapabilities: ConcurrencyCapabilities = SystemInfo) :
    ThreadCountComputer {

    // TODO create a CombinedThreadCountComputer and individual CPU, Memory and Config computers
    // TODO find a better name than ThreadCountComputer

    // region ThreadCountComputer

    override fun getAvailableThreads(commonConfiguration: CommonConfiguration): Int {
        val cpuThreads = concurrencyCapabilities.getCpuParallelCapacity()
        val memoryThreads = concurrencyCapabilities.getMemoryParallelCapacity(commonConfiguration.outputResolution)
        val userThreads = commonConfiguration.simulationMaxThreadCount

        val threadCount = minOf(
            userThreads,
            cpuThreads,
            memoryThreads,
        )

        return threadCount
    }

    // endregion
}
