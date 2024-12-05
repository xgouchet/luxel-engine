package fr.xgouchet.luxels.core.simulation.worker

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.log.progress
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlinx.coroutines.CoroutineName
import kotlinx.datetime.Clock
import kotlin.coroutines.coroutineContext
import kotlin.math.floor
import kotlin.math.max
import kotlin.time.Duration

@Suppress("LongParameterList")
internal class RenderSimulationWorker<D : Dimension, L : Luxel<D>, I : Any>(
    film: Film,
    simulator: Simulator<D, L, I>,
    simulation: Configuration.Simulation<D>,
    projection: Projection<D>,
    time: Duration,
    luxelCountPerThread: Long,
    logHandler: LogHandler,
) : AbstractSimulationWorker<D, L, I>(
    film = film,
    simulator = simulator,
    simulation = simulation,
    projection = projection,
    time = time,
    luxelCountPerThread = luxelCountPerThread,
    logHandler = logHandler,
) {

    private val progressNotification = max(floor(luxelCountPerThread / 1000.0).toLong(), 1)

    // region AbstractSimulationWorker

    override suspend fun simulateSingleLuxel(luxelIndex: Long) {
        val luxel = simulator.spawnLuxel(simulation, time)
        luxel.onStart()

        var step = 0
        do {
            luxel.onStep(step)
            expose(luxel.position(), luxel.color())
            simulator.updateLuxel(luxel, time)
            step++
        } while (luxel.isAlive())

        luxel.onEnd()
        onLuxelRan(luxelIndex)
    }

    // endregion

    // region Internal

    private suspend fun onLuxelRan(i: Long) {
        if (i % progressNotification == 0L) {
            val progress = i.toDouble() / luxelCountPerThread
            val now = Clock.System.now()
            val elapsed = now - frameStart
            val durationPerLuxel = elapsed / i.toDouble()
            val totalDuration = (elapsed * luxelCountPerThread.toDouble()) / i.toDouble()
            val remaining = totalDuration - elapsed
            val workerName = coroutineContext[CoroutineName]?.name ?: "???"
            logHandler.progress(progress, "[$workerName] - $remaining remaining ($durationPerLuxel / luxel)")
        }
    }

    // endregion
}
