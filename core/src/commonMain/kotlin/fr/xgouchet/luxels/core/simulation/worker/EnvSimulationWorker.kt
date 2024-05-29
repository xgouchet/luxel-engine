package fr.xgouchet.luxels.core.simulation.worker

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.random.RandomGenerator
import fr.xgouchet.luxels.core.math.random.inVolume
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

internal class EnvSimulationWorker<D : Dimension, L : Luxel<D>, I : Any>(
    film: Film,
    simulator: Simulator<D, L, I>,
    simulation: Configuration.Simulation<D>,
    projection: Projection<D>,
    time: Duration,
    luxelCountPerThread: Long,
    val rng: RandomGenerator<Vector<D>>,
) : AbstractSimulationWorker<D, L, I>(film, simulator, simulation, projection, time, luxelCountPerThread) {
    // region AbstractSimulationWorker

    override fun simulateSingleLuxel(i: Long) {
        val simulationPosition = rng.inVolume(simulation.volume)

        val color = simulator.environmentColor(simulationPosition, time)

        expose(simulationPosition, color)
    }

    // endregion
}
