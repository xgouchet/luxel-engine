package fr.xgouchet.luxels.core.simulation.worker

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

internal abstract class AbstractSimulationWorker<D : Dimension, L : Luxel<D>, I : Any>(
    protected val film: Film,
    protected val simulator: Simulator<D, L, I>,
    protected val simulation: Configuration.Simulation<D>,
    protected val projection: Projection<D>,
    protected val time: Duration,
    protected val luxelCountPerThread: Long,
) : SimulationWorker {

    protected var frameStart: Instant = Instant.DISTANT_PAST
        private set

    // region AbstractSimulationWorker

    abstract fun simulateSingleLuxel(i: Long)

    fun expose(position: Vector<D>, color: Color) {
        film.expose(projection.convertPosition(position), color)
    }

    // endregion

    // region SimulationWorker

    @Suppress("TooGenericExceptionCaught")
    final override fun work() {
        print("\r  Worker starting on thread (TODO Thread ID)")
        frameStart = Clock.System.now()
        try {
            for (i in 0..luxelCountPerThread) {
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
