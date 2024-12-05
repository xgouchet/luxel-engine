package fr.xgouchet.luxels.core.simulation.worker

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.FrameInfo
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.simulation.Simulator
import fr.xgouchet.luxels.core.simulation.worker.SimulationWorker

internal interface WorkerProvider {
    fun <D : Dimension, L : Luxel<D>, I : Any> createWorker(
        simulator: Simulator<D, L, I>,
        film: Film,
        frameInfo: FrameInfo,
        luxelCountPerThread: Long,
        configuration: Configuration<D, I>,
        logHandler: LogHandler,
    ): SimulationWorker
}