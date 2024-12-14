package fr.xgouchet.luxels.cli.fractals.mandelbrot

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.cli.fractals.Complex
import fr.xgouchet.luxels.components.engine.BaseEnvironment
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.random.inVolume
import fr.xgouchet.luxels.core.math.x
import fr.xgouchet.luxels.core.math.y
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo

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

    override fun updateLuxel(luxel: MandelbrotLuxel) {
    }

    // endregion
}
