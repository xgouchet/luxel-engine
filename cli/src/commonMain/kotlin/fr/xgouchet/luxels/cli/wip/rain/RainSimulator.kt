package fr.xgouchet.luxels.cli.wip.rain

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo

class RainSimulator : Simulator<Dimension.D2, RainLuxel, Environment<Dimension.D2>> {
    // region Simulator
    override fun spawnLuxel(environment: Environment<Dimension.D2>, frameInfo: FrameInfo): RainLuxel {
        TODO("Not yet implemented")
    }

    override fun updateLuxel(
        luxel: RainLuxel,
        environment: Environment<Dimension.D2>,
        frameInfo: FrameInfo,
    ) {
        TODO("Not yet implemented")
    }
    // endregion
}
/*
    val noiseField = VectorNoiseGenerator<Dimension.D2, Dimension.D1>(PerlinNoiseGenerator(), Dimension.D1)

    private var extendedRange: Volume<Dimension.D2> = Volume.unit(Dimension.D2)
    private var safeRange: Volume<Dimension.D2> = Volume.unit(Dimension.D2)

    private var noiseOffset: Vector<Dimension.D2> = Vector.nul(Dimension.D2)
    private var noiseScale: Double = 0.005
    private var noiseThreshold: Double = 0.5

    private var inRI: Double = 0.0
    private var outRI: Double = 0.0

    private var directionalRainSpeed: Vector<Dimension.D2> = Vector.nul(Dimension.D2)
    private var spotRainPosition: Vector<Dimension.D2> = Vector.nul(Dimension.D2)
    private var simulationRange: Volume<Dimension.D2> = Volume.unit(Dimension.D2)

    private var bounceThreshold: Double = 0.0

    private val isDirectional = true

    private var animationDirection: Vector<Dimension.D2> = Vector.nul(Dimension.D2)
    private var animationOffset: Vector<Dimension.D2> = Vector.nul(Dimension.D2)

    // region Simulator

    override fun getProjection(
        simulationVolume: Volume<Dimension.D2>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<Dimension.D2> {
        return Flat2DProjection(simulationVolume, filmSpace)
    }

    override fun initEnvironment(
        simulation: Configuration.Simulation<Dimension.D2>,
        inputData: InputData<Long>,
        logger: LogHandler,
    ) {
        extendedRange = simulation.volume.expanded(2.0)
        safeRange = simulation.volume.expanded(1.2)

        noiseOffset = RndGen.vector2.inVolume(noiseOffsetVolume)

        noiseScale = 1.0 / RndGen.double.inRange(100.0, 500.0)
        noiseThreshold = RndGen.double.uniform()
        inRI = RndGen.double.inRange(1.0, 2.0)
        outRI = RndGen.double.inRange(1.0, 2.0)

        directionalRainSpeed = Vector2(RndGen.double.inRange(-2.0, 2.0), 1.0).normalized()

        spotRainPosition = Vector2(
            RndGen.double.inRange(simulation.volume.min.x, simulation.volume.max.x),
            simulation.volume.min.y - 200,
        )

        bounceThreshold = RndGen.double.inRange(75.0, 100.0)
        simulationRange = simulation.volume

        animationDirection = (RndGen.vector2.uniform() + RndGen.vector2.uniform()).normalized()
    }

    override fun onFrameStart(
        simulation: Configuration.Simulation<Dimension.D2>,
        time: Duration,
        animationDuration: Duration,
    ) {
        animationOffset = animationDirection * time.inWholeMilliseconds.toDouble() * 0.25
    }

    override fun onFrameEnd(time: Duration, animationDuration: Duration) {
    }

    override suspend fun spawnLuxel(simulation: Configuration.Simulation<Dimension.D2>, time: Duration): RainLuxel {
        val randomSpeed: Vector<Dimension.D2>
        val randomPosition: Vector<Dimension.D2>
        if (isDirectional) {
            val halfWidth = simulationRange.size.x / 2.0
            val twentiethHeight = simulationRange.size.y / 20.0
            val range = Volume(
                Vector2(simulationRange.min.x - halfWidth, simulationRange.min.y - twentiethHeight),
                Vector2(simulationRange.max.x + halfWidth, simulationRange.min.y - twentiethHeight),
            )
            randomPosition = RndGen.vector2.inVolume(range)
            randomSpeed = directionalRainSpeed + RndGen.vector2.gaussian()
        } else {
            randomPosition = spotRainPosition + (RndGen.vector2.gaussian() * 100.0)
            randomSpeed = RndGen.vector2.uniform()
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

    override fun environmentColor(position: Vector<Dimension.D2>, time: Duration): HDRColor {
        val ri = getRefractionIndex(position)
        return when (ri) {
            outRI -> HDRColor.BLUE
            inRI -> HDRColor.GREEN
            else -> HDRColor.RED
        }
    }

    override fun outputName(): String {
        return "rain"
    }

    // endregion

    // region Internal

    private fun getRefractionIndex(position: Vector<Dimension.D2>): Double {
        val offset = position + noiseOffset + animationOffset
        val scale = offset * noiseScale
        val noise = noiseField.noise(scale).x
        return if (noise > noiseThreshold) outRI else inRI
    }

    private fun getNormal(position: Vector<Dimension.D2>): Vector<Dimension.D2> {
        var normal = Vector.nul(Dimension.D2)
        val ri = getRefractionIndex(position)
        for (i in 0..<360 step 45) {
            val angle = TAU * i / 360.0
            val offset = fromCircular(angle, 10.0)
            val otherRi = getRefractionIndex(position + offset)
            if (otherRi != ri) {
                normal -= offset
            }
        }
        return normal.normalized()
    }

    // endregion

    companion object {
        val noiseOffsetVolume = Volume(
            Vector2(-65536.0, -65536.0),
            Vector2(65536.0, 65536.0),
        )

        val gravity = Vector2(0.0, 9.81)
    }
}
 */
