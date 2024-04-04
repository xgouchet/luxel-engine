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
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration
import kotlin.time.DurationUnit

class AuroraSimulator : Simulator<AuroraLuxel, Long> {

    private lateinit var element: ASLColorSource
    private var strandInput = 0.0
    private var strandCount = 1

    private val noisePosition = DoubleToVector3NoiseGenerator(FractalNoiseGenerator(PerlinNoiseGenerator(), 4))

    override fun initEnvironment(simulation: Configuration.Simulation, inputData: InputData<Long>) {
//        element = input.data as AtomicElementColorSource
        element = elements[RndGen.int.inRange(0, elements.size)]
        element = Oxygen
        strandCount = (element.number % 3) + 1
        strandInput = RndGen.double.inRange(-1000.0, 1000.0)
        println(" * Random number: ${RndGen.int.uniform()}")
        println(" * initEnvironment: ${element.name} / $strandCount strands")
    }

    override fun spawnLuxel(simulation: Configuration.Simulation, time: Duration): AuroraLuxel {
        val strandOffset = RndGen.int.inRange(0, strandCount) * element.number * 1337.0
        val input = (time.toDouble(DurationUnit.SECONDS) + element.number + strandInput) * 10.0
        val t = RndGen.double.gaussian() + input + strandOffset

        val v = (noisePosition.noise(t) * simulation.space.size)

        return AuroraLuxel(element, v, 1024)
    }

    override fun updateLuxel(luxel: AuroraLuxel, time: Duration) {
        luxel.positionSource.updatePosition(1.0)
    }

    override fun outputName(): String {
        return "aurora"
    }

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
