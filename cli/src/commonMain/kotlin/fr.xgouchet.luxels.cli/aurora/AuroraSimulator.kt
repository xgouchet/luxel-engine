package fr.xgouchet.luxels.cli.aurora

import fr.xgouchet.luxels.core.color.atomic.ASLColorSource
import fr.xgouchet.luxels.core.color.atomic.Hydrogen
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

    private var element: ASLColorSource = Hydrogen

    private val noisePosition = DoubleToVector3NoiseGenerator(FractalNoiseGenerator(PerlinNoiseGenerator(), 4))

    override fun initEnvironment(simulation: Configuration.Simulation, inputData: InputData<Long>) {
//        element = input.data as AtomicElementColorSource
    }

    override fun spawnLuxel(simulation: Configuration.Simulation, time: Duration): AuroraLuxel {
        val input = (time.toDouble(DurationUnit.SECONDS) + element.number) * 10.0
        val t = RndGen.double().uniform() + input

        val v = (noisePosition.noise(t) * simulation.space.size * 0.5)

        return AuroraLuxel(element, v, 1024)
    }

    override fun updateLuxel(luxel: AuroraLuxel, time: Duration) {
        luxel.positionSource.updatePosition(1.0)
    }

    override fun outputName(): String {
        return "aurora"
    }
}
