package art.luxels.engine.simulation.worker

import art.luxels.core.log.LogHandler
import art.luxels.core.log.debug
import art.luxels.core.log.error
import art.luxels.core.log.progress
import art.luxels.core.math.Dimension
import art.luxels.core.render.Exposure
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Luxel
import art.luxels.engine.api.Simulator
import art.luxels.engine.api.configuration.SimulationType
import art.luxels.engine.simulation.CommonConfiguration
import art.luxels.engine.simulation.SceneConfiguration
import art.luxels.engine.simulation.runner.FrameInfo
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
        sceneConfiguration: SceneConfiguration<D, I, E>,
        commonConfiguration: CommonConfiguration,
    ) {
        val coroutineName = coroutineContext[CoroutineName]?.name ?: "???"
        val workerName = "${simulationType.name}|$coroutineName"
        logHandler.debug("Worker [$workerName] starting for simulation $simulationType")
        val frameStart = Clock.System.now()

        val progressNotification = max(commonConfiguration.simulationLuxelCount / 1000L, 2L)

        try {
            simulateAllLuxels(
                exposure,
                sceneConfiguration,
                commonConfiguration,
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
        sceneConfiguration: SceneConfiguration<D, I, E>,
        commonConfiguration: CommonConfiguration,
        progressNotification: Long,
        workerName: String,
        frameStart: Instant,
    ) {
        val context = sceneConfiguration.context ?: error("Context is empty!")
        for (i in 0 until commonConfiguration.simulationLuxelCount) {
            simulateSingleLuxel(
                context.environment,
                exposure,
                i,
                commonConfiguration.animationFrameInfo,
            )

            if (i % progressNotification == 0L) {
                onLuxelRan(workerName, i, commonConfiguration.simulationLuxelCount, frameStart)
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
