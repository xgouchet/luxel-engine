package fr.xgouchet.luxels.engine.simulation.worker

import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.log.debug
import fr.xgouchet.luxels.core.log.error
import fr.xgouchet.luxels.core.log.progress
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.render.Exposure
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.api.configuration.SimulationType
import fr.xgouchet.luxels.engine.simulation.InternalConfiguration
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import kotlinx.coroutines.CoroutineName
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.coroutines.coroutineContext
import kotlin.math.max

/**
 * An abstract [SimulationWorker] to simplify boilerplate logic.
 *
 * @param D the dimension of the space luxels evolve in
 * @param L the type of simulated Luxels
 * @param E the expected Environment
 *
 * @param simulationType the type of simulation
 * @param simulator the simulator to use
 * @param logHandler the [LogHandler] to use throughout the simulation
 */
abstract class AbstractSimulationWorker<D : Dimension, L : Luxel<D>, E : Environment<D>>(
    internal val simulationType: SimulationType,
    internal val simulator: Simulator<D, L, E>,
    internal val logHandler: LogHandler,
) : SimulationWorker<D, E> {

    // region AbstractSimulationWorker

    /**
     * Simulates a single Luxel.
     */
    abstract fun simulateSingleLuxel(
        environment: E,
        exposure: Exposure<D>,
        luxelIndex: Long,
        frameInfo: FrameInfo,
    )

    // endregion

    // region SimulationWorker

    @Suppress("TooGenericExceptionCaught")
    override suspend fun <I : Any> runSimulation(
        exposure: Exposure<D>,
        configuration: InternalConfiguration<D, I, E>,
    ) {
        val coroutineName = coroutineContext[CoroutineName]?.name ?: "???"
        val workerName = "${simulationType.name}|$coroutineName"
        logHandler.debug("Worker [$workerName] starting for simulation $simulationType")
        val frameStart = Clock.System.now()

        val progressNotification = max(configuration.simulationLuxelCount / 1000L, 2L)

        try {
            simulateAllLuxels(
                exposure,
                configuration,
                progressNotification,
                workerName,
                frameStart,
            )
        } catch (t: Throwable) {
            logHandler.error("Worker [$workerName] encountered an error: ${t.message ?: t::class.simpleName}")
            logHandler.error(t.stackTraceToString())
        }

        logHandler.debug("Worker [$workerName] stopping")
    }

    // endregion

    // region Internal

    @Suppress("LongParameterList")
    private fun <I : Any> simulateAllLuxels(
        exposure: Exposure<D>,
        configuration: InternalConfiguration<D, I, E>,
        progressNotification: Long,
        workerName: String,
        frameStart: Instant,
    ) {
        val context = configuration.context ?: error("Context is empty!")
        for (i in 0 until configuration.simulationLuxelCount) {
            simulateSingleLuxel(
                context.environment,
                exposure,
                i,
                configuration.animationFrameInfo,
            )

            if (i % progressNotification == 0L) {
                onLuxelRan(workerName, i, configuration.simulationLuxelCount, frameStart)
            }
        }
    }

    private fun onLuxelRan(
        workerName: String,
        i: Long,
        total: Long,
        frameStart: Instant,
    ) {
        val progress = i.toDouble() / total
        val now = Clock.System.now()
        val elapsed = now - frameStart
        val durationPerLuxel = elapsed / i.toDouble()
        val totalDuration = (elapsed * total.toDouble()) / i.toDouble()
        val remaining = totalDuration - elapsed
        logHandler.progress(progress, "[$workerName] - $remaining remaining ($durationPerLuxel / luxel)")
    }

    // endregion
}
