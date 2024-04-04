package fr.xgouchet.luxels.cli.rain

import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.color.EMSColorSource
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.gen.noise.PerlinNoiseGenerator
import fr.xgouchet.luxels.core.gen.noise.wrapper.Vector3ToDoubleNoiseGenerator
import fr.xgouchet.luxels.core.gen.random.RndGen
import fr.xgouchet.luxels.core.gen.random.inBox
import fr.xgouchet.luxels.core.math.EPSILON
import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.position.Space3
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.math.roundToInt
import kotlin.time.Duration

internal class RainSimulator : Simulator<RainLuxel, Long> {
    val noiseField = Vector3ToDoubleNoiseGenerator(PerlinNoiseGenerator())

    private var extendedRange: Space3 = Space3.UNIT
    private var safeRange: Space3 = Space3.UNIT

    private var noiseOffset: Vector3 = Vector3.NULL
    private var noiseScale: Double = 0.005
    private var noiseThreshold: Double = 0.5

    private var inRI: Double = 0.0
    private var outRI: Double = 0.0

    private var directionalRainSpeed: Vector3 = Vector3.NULL
    private var spotRainPosition: Vector3 = Vector3.NULL
    private var simulationRange: Space3 = Space3.UNIT

    private var bounceThreshold: Double = 0.0

    private val isDirectional = true

    private var animationDirection: Vector3 = Vector3.NULL
    private var animationOffset: Vector3 = Vector3.NULL

    // region Simulator

    override fun initEnvironment(simulation: Configuration.Simulation, inputData: InputData<Long>) {
        extendedRange = simulation.space.expanded(2.0)
        safeRange = simulation.space.expanded(1.2)

        noiseOffset = RndGen.vector3.inBox(noiseOffsetSpace3)

        noiseScale = 1.0 / RndGen.double.inRange(100.0, 500.0)
        noiseThreshold = RndGen.double.uniform()
        inRI = RndGen.double.inRange(1.0, 2.0)
        outRI = RndGen.double.inRange(1.0, 2.0)

        directionalRainSpeed = Vector3(
            RndGen.double.inRange(-2.0, 2.0),
            1.0,
            0.0,
        ).normalized()

        spotRainPosition = Vector3(
            RndGen.double.inRange(simulation.space.min.x, simulation.space.max.x),
            simulation.space.min.y - 200,
            0.0,
        )

        bounceThreshold = RndGen.double.inRange(75.0, 100.0)
        simulationRange = simulation.space

        animationDirection = (RndGen.vector3.uniform() + Vector3(0.0, 0.0, 5.0)).normalized()
    }

    override fun onFrameStart(simulation: Configuration.Simulation, time: Duration) {
        animationOffset = animationDirection * time.inWholeMilliseconds.toDouble() * 0.25
    }

    override fun spawnLuxel(simulation: Configuration.Simulation, time: Duration): RainLuxel {
        val randomSpeed: Vector3
        val randomPosition: Vector3
        if (isDirectional) {
            val halfWidth = simulationRange.size.x / 2.0
            val twentiethHeight = simulationRange.size.y / 20.0
            val range = Space3(
                Vector3(simulationRange.min.x - halfWidth, simulationRange.min.y - twentiethHeight, 0.0),
                Vector3(simulationRange.max.x + halfWidth, simulationRange.min.y - twentiethHeight, 0.0),
            )
            randomPosition = RndGen.vector3.inBox(range)
            randomSpeed = directionalRainSpeed + RndGen.vector3.gaussian()
        } else {
            randomPosition = spotRainPosition + (RndGen.vector3.gaussian() * 100.0)
            randomSpeed = RndGen.vector3.uniform()
        }

        val refractionIndex = getRefractionIndex(randomPosition)
        return RainLuxel(
            RndGen.double.inRange(1024.0, 8192.0).roundToInt(),
            RndGen.double.inRange(
                EMSColorSource.MIN_VISIBLE_LIGHT,
                EMSColorSource.MAX_VISIBLE_LIGHT,
            ),
            randomPosition,
            randomSpeed,
            refractionIndex,
        )
    }

    override fun updateLuxel(luxel: RainLuxel, time: Duration) {
        val position = luxel.position()
        if (position in safeRange) {
            val futurePosition = luxel.positionSource.peekFuturePosition()
            val futureRefractionIndex = getRefractionIndex(futurePosition)
            val n = luxel.refractionIndex / futureRefractionIndex

            if (n != 1.0) {
                val normal = getNormal(position)
                if (RndGen.double.inRange(0.0, 100.0) < bounceThreshold) {
                    luxel.positionSource.updateSpeed(
                        luxel.positionSource.speed.refract(
                            normal,
                            luxel.refractionIndex,
                            futureRefractionIndex,
                            luxel.colorSource.waveLength / EMSColorSource.MEAN_GREEN,
                        ),
                    )
                } else {
                    luxel.positionSource.updateSpeed(
                        luxel.positionSource.speed.reflect(normal),
                    )
                }
                luxel.refractionIndex = futureRefractionIndex
                luxel.colorSource.intensity *= 0.9 // 975
                luxel.colorSource.intensity *= luxel.reductionFactor
            }
        }

        luxel.positionSource.updatePosition()
        luxel.positionSource.updateSpeed(gravity, 20000.0)

        if (!extendedRange.contains(luxel.position())) {
            luxel.lifespanSource.kill()
        }
        if (luxel.colorSource.intensity < EPSILON) {
            luxel.lifespanSource.kill()
        }
    }

    override fun environmentColor(position: Vector3, time: Duration): Color {
        val ri = getRefractionIndex(position)
        return when (ri) {
            outRI -> Color.BLUE
            inRI -> Color.GREEN
            else -> Color.RED
        }
    }

    override fun outputName(): String {
        return "rain"
    }

    // endregion

    // region Internal

    private fun getRefractionIndex(position: Vector3): Double {
        val offset = position + noiseOffset + animationOffset
        val scale = offset * noiseScale
        val noise = noiseField.noise(scale)
        return if (noise > noiseThreshold) outRI else inRI
    }

    private fun getNormal(position: Vector3): Vector3 {
        var normal = Vector3.NULL
        val ri = getRefractionIndex(position)
        for (i in 0 until 360 step 45) {
            val angle = TAU * i / 360.0
            val offset = Vector3.fromSpherical(angle, radius = 10.0)
            val otherRi = getRefractionIndex(position + offset)
            if (otherRi != ri) {
                normal -= offset
            }
        }
        return normal.normalized()
    }

    // endregion

    companion object {
        val noiseOffsetSpace3 = Space3(
            Vector3(-65536.0, -65536.0, 0.0),
            Vector3(65536.0, 65536.0, 0.0),
        )

        val gravity = Vector3(0.0, 9.81, 0.0)
    }
}
