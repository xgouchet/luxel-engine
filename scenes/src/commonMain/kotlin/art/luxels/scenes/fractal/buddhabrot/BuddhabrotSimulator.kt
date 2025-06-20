package art.luxels.scenes.fractal.buddhabrot

import art.luxels.components.engine.BaseEnvironment
import art.luxels.core.color.StaticColorSource
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.random.RndGen
import art.luxels.engine.api.Simulator
import art.luxels.engine.simulation.runner.FrameInfo
import art.luxels.imageio.color.HDRColor

class BuddhabrotSimulator(private val iterations: Int = 0x1_000) :
    Simulator<D2, BuddhabrotLuxel, BaseEnvironment<D2>> {

    // region Simulator

    override fun spawnLuxel(environment: BaseEnvironment<D2>, frameInfo: FrameInfo): BuddhabrotLuxel {
        val (color, lifespan) = when (RndGen.int.inRange(0, 3)) {
            0 -> HDRColor.RED to 0x8_000
            1 -> HDRColor.GREEN to 0x4_000
            2 -> HDRColor.BLUE to 0x2_000
            else -> HDRColor.TRANSPARENT to 1
        }

        return BuddhabrotLuxel(
            lifespan,
            StaticColorSource(color),
            BuddhabrotPositionSource(lifespan),
        )
    }

    override fun updateLuxel(
        luxel: BuddhabrotLuxel,
        environment: BaseEnvironment<D2>,
        frameInfo: FrameInfo,
    ) {
        luxel.positionSource.updatePosition()
    }

    // endregion
}
