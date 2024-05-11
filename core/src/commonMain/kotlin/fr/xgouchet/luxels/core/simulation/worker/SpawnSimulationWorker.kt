package fr.xgouchet.luxels.core.simulation.worker

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

internal class SpawnSimulationWorker<L : Luxel, I : Any>(
    film: Film,
    simulator: Simulator<L, I>,
    simulation: Configuration.Simulation,
    projection: Projection,
    time: Duration,
) : AbstractSimulationWorker<L, I>(film, simulator, simulation, projection, time) {

    // region AbstractSimulationWorker

    override fun simulateSingleLuxel(i: Long) {
        val luxel = simulator.spawnLuxel(simulation, time)
        luxel.onStart()
        expose(luxel.position(), SPAWN_COLOR)
    }

    // endregion

    companion object {
        val SPAWN_COLOR = HDRColor.GREEN
    }
}
