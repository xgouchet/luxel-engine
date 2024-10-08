package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

class StubSimulator<D : Dimension, I : Any> : Simulator<D, StubLuxel<D>, I> {
    override fun getProjection(
        simulationSpace: Volume<D>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<D> {
        TODO("Not yet implemented")
    }

    override suspend fun spawnLuxel(simulation: Configuration.Simulation<D>, time: Duration): StubLuxel<D> {
        TODO("Not yet implemented")
    }

    override fun outputName(): String {
        TODO("Not yet implemented")
    }
}
