package fr.xgouchet.luxels.cli.demo.mandelbrot

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.render.projection.Flat2DProjection
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.log.Logger
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.random.inVolume
import fr.xgouchet.luxels.core.math.x
import fr.xgouchet.luxels.core.math.y
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

internal class MandelbrotSimulator(
    private val iterations: Int = 0x1_000,
) : Simulator<Dimension.D2, MandelbrotLuxel, Unit> {

    private var simulationSpace: Volume<Dimension.D2> = Volume.unit(Dimension.D2)

    // region Simulator

    override fun getProjection(
        simulationSpace: Volume<Dimension.D2>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<Dimension.D2> {
        return Flat2DProjection(simulationSpace, filmSpace)
    }

    override fun initEnvironment(
        simulation: Configuration.Simulation<Dimension.D2>,
        inputData: InputData<Unit>,
        logger: Logger,
    ) {
        this.simulationSpace = simulation.volume
    }

    override suspend fun spawnLuxel(
        simulation: Configuration.Simulation<Dimension.D2>,
        time: Duration,
    ): MandelbrotLuxel {
        val vec = RndGen.vector2.inVolume(simulationSpace)
        val (col, iter) = when (RndGen.int.inRange(0, 3)) {
            0 -> HDRColor.RED to iterations * 2
            1 -> HDRColor.GREEN to iterations
            2 -> HDRColor.BLUE to iterations / 2
            else -> TODO()
        }

        return MandelbrotLuxel(
            Complex(vec.x, vec.y),
            col,
            iter,
        )
    }

    override fun updateLuxel(luxel: MandelbrotLuxel, time: Duration) {
    }

    override fun outputName(): String {
        return "mandelbrot"
    }

    // endregion
}
