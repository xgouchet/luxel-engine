package fr.xgouchet.luxels.cli.demo.projection

import fr.xgouchet.luxels.cli.demo.DemoLuxel
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

internal abstract class NdCubeSimulator<D : Dimension> : Simulator<D, DemoLuxel<D>, Long> {

    // region Simulator

    override fun getProjection(
        simulationSpace: Volume<D>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<D> {
        TODO()
    }

    @Suppress("CyclomaticComplexMethod")
    override suspend fun spawnLuxel(simulation: Configuration.Simulation<D>, time: Duration): DemoLuxel<D> {
        TODO()
    }

    // endregion
}
