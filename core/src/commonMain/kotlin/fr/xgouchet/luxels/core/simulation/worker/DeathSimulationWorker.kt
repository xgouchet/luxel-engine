package fr.xgouchet.luxels.core.simulation.worker

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.log.Logger
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

@Suppress("LongParameterList")
internal class DeathSimulationWorker<D : Dimension, L : Luxel<D>, I : Any>(
    film: Film,
    simulator: Simulator<D, L, I>,
    simulation: Configuration.Simulation<D>,
    projection: Projection<D>,
    time: Duration,
    luxelCountPerThread: Long,
    logger: Logger,
) : AbstractSimulationWorker<D, L, I>(
    film = film,
    simulator = simulator,
    simulation = simulation,
    projection = projection,
    time = time,
    luxelCountPerThread = luxelCountPerThread,
    logger = logger,
) {

    // region AbstractSimulationWorker

    override suspend fun simulateSingleLuxel(i: Long) {
        val luxel = simulator.spawnLuxel(simulation, time)
        luxel.onStart()

        var step = 0
        while (luxel.isAlive()) {
            luxel.onStep(step)
            simulator.updateLuxel(luxel, time)
            step++
        }

        expose(luxel.position(), DEATH_COLOR)
        luxel.onEnd()
    }

    // endregion

    companion object {
        val DEATH_COLOR = HDRColor.RED
    }
}
