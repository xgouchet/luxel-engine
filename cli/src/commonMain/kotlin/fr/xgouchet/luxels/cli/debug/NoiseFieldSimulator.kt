package fr.xgouchet.luxels.cli.debug

import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.gen.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.core.gen.noise.FractalNoiseGenerator.Companion.brownNoise
import fr.xgouchet.luxels.core.gen.noise.PerlinNoiseGenerator
import fr.xgouchet.luxels.core.gen.noise.wrapper.Vector3ToDoubleNoiseGenerator
import fr.xgouchet.luxels.core.gen.random.RndGen
import fr.xgouchet.luxels.core.gen.random.inBox
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

class NoiseFieldSimulator() : Simulator<NoiseFieldLuxel, Long> {

    val baseNoise: DimensionalNoiseGenerator = brownNoise(PerlinNoiseGenerator())

    //    val brownian = FractalBrownianNoise(baseNoise, 1, 0.5)
    val vectorToDouble = Vector3ToDoubleNoiseGenerator(baseNoise)

    private var noiseScale = 0.005

    private var noiseOffset = Vector3.NULL

    override fun initEnvironment(simulation: Configuration.Simulation, inputData: InputData<Long>) {
        noiseOffset = RndGen.vector3.inBox(simulation.space)
        noiseScale = RndGen.double.inRange(0.001, 0.01)
    }

    override fun onFrameStart(simulation: Configuration.Simulation, time: Duration) {
        noiseOffset = Vector3(0.0, 0.0, time.inWholeMilliseconds * 0.001)
    }

    override fun spawnLuxel(simulation: Configuration.Simulation, time: Duration): NoiseFieldLuxel {
        val position = RndGen.vector3.inBox(simulation.space)
        val color = vectorToDouble.noise((position * noiseScale) + noiseOffset)

        return NoiseFieldLuxel(position, Color(color, color, color))
    }

    override fun outputName(): String {
        return "noise"
    }

    override fun updateLuxel(luxel: NoiseFieldLuxel, time: Duration) {
        // val angle = vectorToDouble.noise((luxel.position() * noiseScale) + noiseOffset) * TAU
        // val speed = Vector3.fromSpherical(angle)
//        imageColorSource.uv = (luxel.position() / simulationSpace.max).xy

//        val saturation = luxel.color().saturation()
//        luxel.positionSource.updateSpeed(speed, 1.0 / (1.0 + saturation))
//        luxel.positionSource.updatePosition(0.2)
    }
}
