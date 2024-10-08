package fr.xgouchet.luxels.core.simulation.worker

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.log.Logger
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlinx.coroutines.CoroutineName
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration

@Suppress("LongParameterList")
internal abstract class AbstractSimulationWorker<D : Dimension, L : Luxel<D>, I : Any>(
    internal val film: Film,
    internal val simulator: Simulator<D, L, I>,
    internal val simulation: Configuration.Simulation<D>,
    internal val projection: Projection<D>,
    internal val time: Duration,
    internal val luxelCountPerThread: Long,
    internal val logger: Logger,
) : SimulationWorker {

    protected var frameStart: Instant = Instant.DISTANT_PAST
        private set

    // region AbstractSimulationWorker

    abstract suspend fun simulateSingleLuxel(i: Long)

    fun expose(position: Vector<D>, color: Color) {
        film.expose(projection.convertPosition(position), color)
    }

    // endregion

    // region SimulationWorker

    @Suppress("TooGenericExceptionCaught")
    final override suspend fun work() {
        val name = coroutineContext[CoroutineName]?.name ?: "???"
        logger.debug("Worker [$name] starting")
        frameStart = Clock.System.now()
        try {
            for (i in 0..luxelCountPerThread) {
                simulateSingleLuxel(i)
            }
        } catch (t: Throwable) {
            logger.error("Error while simulating Luxel on Worker [$name]: ${t.message ?: t::class.simpleName}")
        }

        logger.debug("\r  Worker [$name] stopping")
    }

    // endregion
}
