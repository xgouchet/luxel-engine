package art.luxels.cli.wip.rain

import art.luxels.cli.wip.rain.RainEnvironment.Companion.gravity
import art.luxels.components.color.EMSColorSource
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.EPSILON
import art.luxels.core.math.Vector2
import art.luxels.core.math.Volume
import art.luxels.core.math.random.RndGen
import art.luxels.core.math.random.inVolume
import art.luxels.core.math.x
import art.luxels.core.math.y
import art.luxels.engine.api.Simulator
import art.luxels.engine.simulation.runner.FrameInfo
import kotlin.math.roundToInt

class RainSimulator : Simulator<D2, RainLuxel, RainEnvironment> {

    // region Simulator

    override fun spawnLuxel(environment: RainEnvironment, frameInfo: FrameInfo): RainLuxel {
        val halfWidth = environment.simulationVolume.size.x / 2.0
        val smallOffset = 1.0
        val range = Volume(
            Vector2(
                environment.simulationVolume.min.x - halfWidth,
                environment.simulationVolume.min.y - smallOffset,
            ),
            Vector2(
                environment.simulationVolume.max.x + halfWidth,
                environment.simulationVolume.min.y - smallOffset,
            ),
        )
        val randomPosition = RndGen.vector2.inVolume(range)
        val randomSpeed = environment.directionalRainSpeed + RndGen.vector2.gaussian()

        val refractionIndex = environment.getRefractionIndex(randomPosition)


        return RainLuxel(
            RndGen.double.inRange(1024.0, 8192.0).roundToInt(),
            RndGen.double.inRange(
                environment.lightWaveLengthCenter - environment.lightWaveLengthWidth,
                environment.lightWaveLengthCenter + environment.lightWaveLengthWidth,
            ),
            randomPosition,
            randomSpeed,
            refractionIndex,
        )
    }

    override fun updateLuxel(
        luxel: RainLuxel,
        environment: RainEnvironment,
        frameInfo: FrameInfo,
    ) {
        val position = luxel.position()
        if (position in environment.safeRange) {
            val futurePosition = luxel.positionSource.peekFuturePosition()
            val futureRefractionIndex = environment.getRefractionIndex(futurePosition)
            val n = luxel.refractionIndex / futureRefractionIndex

            if (n != 1.0) {
                val normal = environment.getNormal(position)
                if (RndGen.double.inRange(0.0, 100.0) < environment.bounceThreshold) {
                    val refractionStrength = luxel.colorSource.waveLength / EMSColorSource.MEAN_GREEN
                    luxel.positionSource.updateSpeed(
                        luxel.positionSource.speed.refract(
                            normal,
                            luxel.refractionIndex * refractionStrength,
                            futureRefractionIndex,
                        ),
                    )
                } else {
                    luxel.positionSource.updateSpeed(
                        luxel.positionSource.speed.reflect(normal),
                    )
                }
                luxel.refractionIndex = futureRefractionIndex
                luxel.colorSource.intensity *= 0.95 // 975
                luxel.colorSource.intensity *= luxel.reductionFactor
            }
        }

        luxel.positionSource.updatePosition()
        luxel.positionSource.updateSpeed(gravity, 20000.0)

        if (!environment.extendedRange.contains(luxel.position())) {
            luxel.lifespanSource.kill()
        }
        if (luxel.colorSource.intensity < EPSILON) {
            luxel.lifespanSource.kill()
        }
    }

    // endregion
}
