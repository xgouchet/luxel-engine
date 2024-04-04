package fr.xgouchet.luxels.cli.aurora

import fr.xgouchet.luxels.core.color.atomic.ASLColorSource
import fr.xgouchet.luxels.core.color.atomic.Aluminium
import fr.xgouchet.luxels.core.color.atomic.Carbon
import fr.xgouchet.luxels.core.color.atomic.Copper
import fr.xgouchet.luxels.core.color.atomic.Helium
import fr.xgouchet.luxels.core.color.atomic.Hydrogen
import fr.xgouchet.luxels.core.color.atomic.Iron
import fr.xgouchet.luxels.core.color.atomic.Nitrogen
import fr.xgouchet.luxels.core.color.atomic.Oxygen
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.gen.noise.FractalNoiseGenerator
import fr.xgouchet.luxels.core.gen.noise.PerlinNoiseGenerator
import fr.xgouchet.luxels.core.gen.noise.wrapper.DoubleToVector3NoiseGenerator
import fr.xgouchet.luxels.core.gen.random.RndGen
import fr.xgouchet.luxels.core.gen.random.inBox
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.position.Space2
import fr.xgouchet.luxels.core.position.Space3
import fr.xgouchet.luxels.core.render.projection.PerspectiveProjection
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.math.PI
import kotlin.time.Duration
import kotlin.time.DurationUnit

internal class AuroraSimulator : Simulator<AuroraLuxel, Long> {

    private var element: ASLColorSource = Hydrogen
    private var strandInput = 0.0
    private var strandCount = 1
    private var strandOffsets: List<Vector3> = emptyList()

    private val noisePosition = DoubleToVector3NoiseGenerator(FractalNoiseGenerator(PerlinNoiseGenerator(), 4))

    // region Simulator
    override fun getProjection(simulationSpace: Space3, filmSpace: Space2, time: Duration): Projection {
        val camOffset = Vector3.fromSpherical(time.inWholeMilliseconds * PI * 0.0001)
        return PerspectiveProjection(
            simulationSpace,
            filmSpace,
            simulationSpace.center + (camOffset * simulationSpace.size.length()),
        )
    }

    override fun initEnvironment(simulation: Configuration.Simulation, inputData: InputData<Long>) {
//        element = input.data as AtomicElementColorSource
        element = elements[RndGen.int.inRange(0, elements.size)]
        element = Oxygen
        strandCount = (element.number % 3) + 1
        strandInput = RndGen.double.inRange(-1000.0, 1000.0)
        strandOffsets = (0..<strandCount).map {
            RndGen.vector3.inBox(simulation.space)
        }
    }

    override fun spawnLuxel(simulation: Configuration.Simulation, time: Duration): AuroraLuxel {
        val strandIndex = RndGen.int.inRange(0, strandCount)
        val strandOffset = strandIndex * element.number * 1337.0
        val input = (time.toDouble(DurationUnit.SECONDS) + element.number + strandInput) * 10.0
        val t = (RndGen.double.gaussian() * 0.5) + input + strandOffset

        val initialPosition = (noisePosition.noise(t) * simulation.space.size) + strandOffsets[strandIndex]
        val randomVector3 = RndGen.vector3.uniform().normalized()
        val tangent = tangent(t)
        val initialSpeed = (tangent cross randomVector3)

        return AuroraLuxel(
            element,
            initialPosition,
            initialSpeed,
            1024,
        )
    }

    override fun updateLuxel(luxel: AuroraLuxel, time: Duration) {
        luxel.positionSource.updatePosition(1.0)
    }

    override fun outputName(): String {
        return "aurora"
    }

    // endregion

    // region Internal

    private fun tangent(t: Double): Vector3 {
        return noisePosition.noise(t + 0.1) - noisePosition.noise(t - 0.1)
    }

    // endregion

    companion object {
        private val elements = arrayOf(
            Hydrogen,
            Helium,
            Carbon,
            Oxygen,
            Nitrogen,
            Aluminium,
            Iron,
            Copper,
        )
    }
}
