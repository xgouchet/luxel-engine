package fr.xgouchet.luxels.cli.buddhabrot

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.color.StaticColorSource
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.math.geometry.Space3
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

internal class BuddhabrotSimulator : Simulator<BuddhabrotLuxel, Unit> {
    var simulationSpace: Space3 = Space3.UNIT

    // region Simulator

    override fun initEnvironment(simulation: Configuration.Simulation, inputData: InputData<Unit>) {
        this.simulationSpace = simulation.space
    }

    override fun spawnLuxel(simulation: Configuration.Simulation, time: Duration): BuddhabrotLuxel {
        val (color, lifespan) = when (RndGen.int.inRange(0, 3)) {
            0 -> HDRColor.RED to 0x4_000
            1 -> HDRColor.GREEN to 0x2_000
            2 -> HDRColor.BLUE to 0x1_000
            else -> HDRColor.TRANSPARENT to 1
        }

        return BuddhabrotLuxel(
            lifespan,
            StaticColorSource(color),
            BuddhabrotPositionSource(lifespan, simulationSpace),
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
