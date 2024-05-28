package fr.xgouchet.luxels.cli.demo.metaballs

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

// TODO higher dimensions?
internal class MetaBallsSimulator : Simulator<Dimension.D3, Luxel<Dimension.D3>, Unit> {

    // region Simulator

    override fun getProjection(
        simulationSpace: Volume<Dimension.D3>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<Dimension.D3> {
        TODO("Not yet implemented: getProjection")
    }

    override fun spawnLuxel(simulation: Configuration.Simulation<Dimension.D3>, time: Duration): Luxel<Dimension.D3> {
        TODO("Not yet implemented: spawnLuxel")
    }

    override fun outputName(): String {
        return "metaballs"
    }

    // endregion
}
