package art.luxels.cli.wip.aurora

import art.luxels.core.math.Dimension.D3
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Simulator
import art.luxels.engine.simulation.runner.FrameInfo

class AuroraSimulator : Simulator<D3, AuroraLuxel, Environment<D3>> {
    // region Simulator
    override fun spawnLuxel(environment: Environment<D3>, frameInfo: FrameInfo): AuroraLuxel {
        TODO("Not yet implemented")
    }

    override fun updateLuxel(
        luxel: AuroraLuxel,
        environment: Environment<D3>,
        frameInfo: FrameInfo,
    ) {
        TODO("Not yet implemented")
    }
    // endregion
}
/*

    private var element: ASLColorSource = Hydrogen
    private var strandInput = 0.0
    private var strandCount = 1
    private var strandOffsets: List<Vector<Dimension.D3>> = emptyList()

    private var skyOffset: Vector<Dimension.D3> = Vector.nul(Dimension.D3)

    val brownNoise = FractalNoiseGenerator.brownNoise(PerlinNoiseGenerator(), octaves = 3)
    val whiteNoise = PerlinNoiseGenerator() // FractalNoiseGenerator.whiteNoise(PerlinNoiseGenerator())

    // TODO ensure on a large enough range the result will always be from 0.0 to 1.0 ?
    private val noisePosition = DoubleToVectorNoiseGenerator(brownNoise, Dimension.D3)

    private val atmosphere = VectorToDoubleNoiseGenerator<Dimension.D3>(whiteNoise)

    var minIntensity = Double.MAX_VALUE
    var maxIntensity = Double.MIN_VALUE

    // region Simulator

    override fun getProjection(
        simulationVolume: Volume<Dimension.D3>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<Dimension.D3> {
        val camOffset = fromSpherical(time.inWholeMilliseconds * PI * 0.0001)
        return PerspectiveProjection(
            simulationVolume,
            filmSpace,
            simulationVolume.center + (camOffset * simulationVolume.size.length()),
        )
    }

    override fun initEnvironment(
        simulation: Configuration.Simulation<Dimension.D3>,
        inputData: InputData<Long>,
        logger: LogHandler,
    ) {
        element = elements[RndGen.int.inRange(0, elements.size)]
        println("Aurora with element ${element.name}")
        strandCount = (element.number % 3) + 1
        strandInput = RndGen.double.inRange(-1000.0, 1000.0)
        strandOffsets = (0..<strandCount).map {
            RndGen.vector3.inVolume(simulation.volume)
        }
        skyOffset = Vector3(0.0, simulation.volume.size.y, 0.0) * 2.0
    }

    override suspend fun spawnLuxel(simulation: Configuration.Simulation<Dimension.D3>, time: Duration): AuroraLuxel {
        val strandIndex = RndGen.int.inRange(0, strandCount)
        val strandOffset = strandIndex * element.number * 1337.0
        val input = (time.toDouble(DurationUnit.SECONDS) + element.number + strandInput) * 10.0
        val t = (RndGen.double.gaussian() * 0.5) + input + strandOffset

        val initialPosition = (noisePosition.noise(t) * simulation.volume.size) + strandOffsets[strandIndex] - skyOffset
        val randomVector3 = RndGen.vector3.uniform().normalized()
        val tangent = tangent(t)
        val initialSpeed = (tangent cross randomVector3)
        val fuzzyRadius = 5.0

        return AuroraLuxel(
            element,
            initialPosition,
            initialSpeed,
            fuzzyRadius,
            2048,
        )
    }

    override fun updateLuxel(luxel: AuroraLuxel, time: Duration) {
        luxel.positionSource.updatePosition(1.0)
        luxel.intensity = atmosphere.noise(luxel.position() / 1000.0)
        minIntensity = min(luxel.intensity, minIntensity)
        maxIntensity = max(luxel.intensity, minIntensity)
//        println("luxel.intensity:${luxel.intensity}")
    }

    override fun outputName(): String {
        return "aurora"
    }

    // endregion

    // region Internal

    private fun tangent(t: Double): Vector<Dimension.D3> {
        return noisePosition.noise(t + 0.1) - noisePosition.noise(t - 0.1)
    }

    // endregion

    companion object {
        private val elements = PeriodicTable.lightElements
    }
}
 */
