package art.luxels.cli.fractals.mandelbrot

import art.luxels.cli.fractals.Complex
import art.luxels.components.engine.BaseEnvironment
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.random.RndGen
import art.luxels.core.math.random.inVolume
import art.luxels.core.math.x
import art.luxels.core.math.y
import art.luxels.engine.api.Simulator
import art.luxels.engine.simulation.runner.FrameInfo
import art.luxels.imageio.color.HDRColor

class MandelbrotSimulator(
    private val iterations: Int = 0x1_000,
) : Simulator<D2, MandelbrotLuxel, BaseEnvironment<D2>> {

    // region Simulator

    override fun spawnLuxel(environment: BaseEnvironment<D2>, frameInfo: FrameInfo): MandelbrotLuxel {
        val vec = RndGen.vector2.inVolume(environment.simulationVolume)
        val (col, iter) = when (RndGen.int.inRange(0, 3)) {
            0 -> HDRColor.RED to iterations * 2
            1 -> HDRColor.GREEN to iterations
            2 -> HDRColor.BLUE to iterations / 2
            else -> TODO()
        }

        return MandelbrotLuxel(Complex(vec.x, vec.y), col, iter)
    }

    override fun updateLuxel(
        luxel: MandelbrotLuxel,
        environment: BaseEnvironment<D2>,
        frameInfo: FrameInfo,
    ) {
    }

    // endregion
}
