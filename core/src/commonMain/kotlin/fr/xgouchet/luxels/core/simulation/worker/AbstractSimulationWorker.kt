package fr.xgouchet.luxels.core.simulation.worker

import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

internal abstract class AbstractSimulationWorker<L : Luxel, I : Any>(
    protected val film: Film,
    protected val simulator: Simulator<L, I>,
    protected val simulation: Configuration.Simulation,
    private val projection: Projection,
    protected val time: Duration,
) : SimulationWorker {

    // region AbstractSimulationWorker

    abstract fun simulateSingleLuxel(i: Long)

    fun expose(position: Vector3, color: Color) {
        film.expose(projection.convertPosition(position), color)
    }

    // region Runnable

    @Suppress("TooGenericExceptionCaught")
    final override fun work() {
        print("\r  Worker starting on thread (TODO Thread ID)")

        try {
            for (i in 0..simulation.luxelPerThread) {
                simulateSingleLuxel(i)
            }
        } catch (t: Throwable) {
            println("\r  ✘ Error while simulating Luxel: ${t.message ?: t::class.simpleName}")
            t.printStackTrace()
        }

        print("\r  Worker stopping on thread (TODO Thread ID)")
    }

    // endregion
}
