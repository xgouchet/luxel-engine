package fr.xgouchet.luxels.cli.aurora

import fr.xgouchet.luxels.components.color.atomic.ASLColorSource
import fr.xgouchet.luxels.components.color.atomic.Hydrogen
import fr.xgouchet.luxels.components.color.atomic.PeriodicTable
import fr.xgouchet.luxels.components.noise.FractalNoiseGenerator
import fr.xgouchet.luxels.components.noise.PerlinNoiseGenerator
import fr.xgouchet.luxels.components.noise.wrapper.DoubleToVectorNoiseGenerator
import fr.xgouchet.luxels.components.noise.wrapper.VectorToDoubleNoiseGenerator
import fr.xgouchet.luxels.components.render.projection.PerspectiveProjection
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.cross
import fr.xgouchet.luxels.core.math.fromSpherical
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.random.inVolume
import fr.xgouchet.luxels.core.math.y
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.DurationUnit

internal class AuroraSimulator : Simulator<Dimension.D3, AuroraLuxel, Long> {

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

    override fun onFrameEnd(time: Duration, animationDuration: Duration) {
        println("minIntensity:$minIntensity")
        println("maxIntensity:$maxIntensity")
    }

    override fun getProjection(
        simulationSpace: Volume<Dimension.D3>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<Dimension.D3> {
        val camOffset = fromSpherical(time.inWholeMilliseconds * PI * 0.0001)
        return PerspectiveProjection(
            simulationSpace,
            filmSpace,
            simulationSpace.center + (camOffset * simulationSpace.size.length()),
        )
    }

    override fun initEnvironment(simulation: Configuration.Simulation<Dimension.D3>, inputData: InputData<Long>) {
        element = elements[RndGen.int.inRange(0, elements.size)]
        println("Aurora with element ${element.name}")
        strandCount = (element.number % 3) + 1
        strandInput = RndGen.double.inRange(-1000.0, 1000.0)
        strandOffsets = (0..<strandCount).map {
            RndGen.vector3.inVolume(simulation.volume)
        }
        skyOffset = Vector3(0.0, simulation.volume.size.y, 0.0) * 2.0
    }

    override fun spawnLuxel(simulation: Configuration.Simulation<Dimension.D3>, time: Duration): AuroraLuxel {
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
