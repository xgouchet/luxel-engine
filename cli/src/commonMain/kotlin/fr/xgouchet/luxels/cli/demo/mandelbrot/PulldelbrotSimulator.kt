package fr.xgouchet.luxels.cli.demo.mandelbrot

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.render.projection.Flat2DProjection
import fr.xgouchet.luxels.core.color.StaticColorSource
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

internal class PulldelbrotSimulator(
    private val iterations: Int = 0x1_000,
) : Simulator<Dimension.D2, PulldelbrotLuxel, Unit> {

    private var simulationSpace: Volume<Dimension.D2> = Volume.unit(Dimension.D2)

    // region Simulator

    override fun getProjection(
        simulationSpace: Volume<Dimension.D2>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<Dimension.D2> {
        return Flat2DProjection(simulationSpace, filmSpace)
    }

    override fun initEnvironment(simulation: Configuration.Simulation<Dimension.D2>, inputData: InputData<Unit>) {
        this.simulationSpace = simulation.volume
    }

    override fun spawnLuxel(simulation: Configuration.Simulation<Dimension.D2>, time: Duration): PulldelbrotLuxel {
        TODO("spawnLuxel")
    }

    override fun updateLuxel(luxel: PulldelbrotLuxel, time: Duration) {
        TODO("updateLuxel")
    }

    override fun outputName(): String {
        return "pulldelbrot"
    }

    // endregion
}
