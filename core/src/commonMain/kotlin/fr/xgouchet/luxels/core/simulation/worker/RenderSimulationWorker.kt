package fr.xgouchet.luxels.core.simulation.worker

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlinx.datetime.Clock
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.time.Duration

internal class RenderSimulationWorker<L : Luxel, I : Any>(
    film: Film,
    simulator: Simulator<L, I>,
    simulation: Configuration.Simulation,
    projection: Projection,
    time: Duration,
) : AbstractSimulationWorker<L, I>(film, simulator, simulation, projection, time) {

    private val frameStart = Clock.System.now()

    val progressNotif = max(floor(simulation.luxelPerThread / 1000.0).toLong(), 1)

    // region AbstractSimulationWorker

    override fun simulateSingleLuxel(i: Long) {
        val luxel = simulator.spawnLuxel(simulation, time)
        luxel.onStart()

        var step = 0
        while (luxel.isAlive()) {
            luxel.onStep(step)
            expose(luxel.position(), luxel.color())
            simulator.updateLuxel(luxel, time)
            step++
        }

        luxel.onEnd()
        onLuxelRan(i)
    }

    // endregion

    // region Internal

    private fun onLuxelRan(i: Long) {
        if (i % progressNotif == 0L) {
            val progress = (i * 1000.0) / simulation.luxelPerThread
            val elapsed = Clock.System.now() - frameStart
            val totalDuration = (elapsed * simulation.luxelPerThread.toDouble()) / i.toDouble()
            val remaining = totalDuration - elapsed
            print("\r    … ${progress.roundToInt()}‰ [thread#(TODO Thread ID)] - $remaining remaining")
        }
    }

    // endregion
}
