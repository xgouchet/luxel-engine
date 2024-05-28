package fr.xgouchet.luxels.cli.demo.buddhabrot

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

internal class BuddhabrotSimulator(
    private val iterations: Int = 0x1_000,
) : Simulator<Dimension.D2, BuddhabrotLuxel, Unit> {

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
        this.simulationSpace = simulation.space
    }

    override fun spawnLuxel(simulation: Configuration.Simulation<Dimension.D2>, time: Duration): BuddhabrotLuxel {
        val (color, lifespan) = when (RndGen.int.inRange(0, 3)) {
            0 -> HDRColor.RED to (iterations * 4)
            1 -> HDRColor.GREEN to (iterations * 3)
            2 -> HDRColor.BLUE to (iterations * 2)
            else -> HDRColor.TRANSPARENT to 1
        }

        return BuddhabrotLuxel(
            lifespan,
            StaticColorSource(color),
            BuddhabrotPositionSource(lifespan),
        )
    }

    override fun outputName(): String {
        return "buddhabrot"
    }

    override fun updateLuxel(luxel: BuddhabrotLuxel, time: Duration) {
        luxel.positionSource.updatePosition()
    }

    // endregion
}
