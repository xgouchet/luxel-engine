package fr.xgouchet.luxels.cli.series.s02pixie

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.random.inVolume
import fr.xgouchet.luxels.core.math.x
import fr.xgouchet.luxels.core.math.y
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import kotlin.time.Duration.Companion.seconds

class PixieSimulator : Simulator<Dimension.D2, PixieLuxel, PixieEnvironment> {

    // region Simulator

    override fun spawnLuxel(environment: PixieEnvironment, frameInfo: FrameInfo): PixieLuxel {
        val position = RndGen.vector2.inVolume(environment.simulationVolume)
        val initialColor = environment.environmentColor(position, frameInfo.time)

        val (colorMask, iteration) = when (RndGen.int.uniform() % 3) {
            0 -> (HDRColor.RED) to 128
            1 -> (HDRColor.GREEN) to 96
            2 -> (HDRColor.BLUE) to 256
            else -> HDRColor.TRANSPARENT to 1
        }

        return PixieLuxel(colorMask, position, iteration, initialColor)
    }

    override fun updateLuxel(
        luxel: PixieLuxel,
        environment: PixieEnvironment,
        frameInfo: FrameInfo,
    ) {
        val color = luxel.color

        // find a position in a [-½, ½]  range
        val homogenousPos = (luxel.position() - environment.simulationVolume.center) / environment.simulationVolume.size

        // create a direction update in a [-1, 1] range
        val homogenousDir = Vector2(
            (color.hue() * 2.0) - 1.0,
            (color.value() * 2.0) - 1.0,
        )
        val base = 1.0
        val offset = 0.01
        val magnitude = (base - (offset / 2.0)) + (offset * luxel.lifespanSource.progression)

        // find a position in a [-1, 1]  range
        val newPos = keepInRange(homogenousPos + (homogenousDir * magnitude * color.saturation()))

        luxel.position = (newPos * environment.simulationVolume.size) + environment.simulationVolume.center
        luxel.nextColor = environment.environmentColor(luxel.position, 0.seconds)
    }

    // endregion

    // region Internal

    private fun keepInRange(value: Vector<Dimension.D2>): Vector<Dimension.D2> {
        return Vector2(
            keepInRange(value.x),
            keepInRange(value.y),
        )
    }

    private fun keepInRange(value: Double): Double {
        if (value < -1.0) {
            var v = value
            while (v < -1.0) v += 2.0
            return v
        }
        if (value > 1.0) {
            var v = value
            while (v > 1.0) v -= 2.0
            return v
        }
        return value
    }

    // endregion
}
