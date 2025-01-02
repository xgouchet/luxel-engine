package fr.xgouchet.luxels.engine.simulation.runner

import fr.xgouchet.luxels.core.concurrency.ConcurrencyCapabilities
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.system.SystemInfo
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.simulation.InternalConfiguration

/**
 * Default implementation that the following constraints into account.
 * @param concurrencyCapabilities provider to know the capabilities of the current runtime
 */
class DefaultThreadCountComputer(
    val concurrencyCapabilities: ConcurrencyCapabilities = SystemInfo,
) : ThreadCountComputer {

    // TODO create a CombinedThreadCountComputer and individual CPU, Memory and Config computers
    // TODO find a better name than ThreadCountComputer

    // region ThreadCountComputer

    override fun <D : Dimension, I : Any, E : Environment<D>> getAvailableThreads(
        configuration: InternalConfiguration<D, I, E>,
    ): Int {
        val cpuThreads = concurrencyCapabilities.getCpuParallelCapacity()
        val memoryThreads = concurrencyCapabilities.getMemoryParallelCapacity(configuration.outputResolution)
        val userThreads = configuration.simulationMaxThreadCount

        val threadCount = minOf(
            userThreads,
            cpuThreads,
            memoryThreads,
        )

        return threadCount
    }

    // endregion
}
