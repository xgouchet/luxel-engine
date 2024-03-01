package fr.xgouchet.luxels.cli.debug

import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.gen.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.core.gen.noise.FractalNoiseGenerator
import fr.xgouchet.luxels.core.gen.noise.PerlinNoiseGenerator
import fr.xgouchet.luxels.core.gen.noise.PingPongNoiseGenerator
import fr.xgouchet.luxels.core.gen.noise.wrapper.Vector3ToDoubleNoiseGenerator
import fr.xgouchet.luxels.core.gen.noise.wrapper.Vector3ToVector3NoiseGenerator
import fr.xgouchet.luxels.core.gen.random.RndGen
import fr.xgouchet.luxels.core.gen.random.inBox
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

class DebugSimulator() : Simulator<DebugLuxel, Long> {

    val baseNoise: DimensionalNoiseGenerator = PerlinNoiseGenerator()
    val ridged = PingPongNoiseGenerator(baseNoise)
    val brownian = FractalNoiseGenerator(ridged, 4)
    val vectorToDouble = Vector3ToDoubleNoiseGenerator(brownian)
    val vectorToVector = Vector3ToVector3NoiseGenerator(brownian)

    private val noiseScale = Vector3(0.005, 0.005, 0.005)

    private var noiseOffset = Vector3.NULL

    override fun onFrameStart(simulation: Configuration.Simulation, time: Duration) {
        noiseOffset = Vector3(0.0, 0.0, time.inWholeMilliseconds * 0.001)
    }

    override fun spawnLuxel(simulation: Configuration.Simulation, time: Duration): DebugLuxel {
        val position = RndGen.vector3().inBox(simulation.space)
//        val noise = vectorToVector.noise(position * noiseScale)
//        val color = Color(noise.x, noise.y, noise.z)
        val noise = vectorToDouble.noise((position * noiseScale) + noiseOffset)
        val color = Color(noise, noise, noise)

        return DebugLuxel(
            position,
            color,
        )
    }

    override fun outputName(): String {
        return "debug"
    }

    override fun updateLuxel(luxel: DebugLuxel, time: Duration) {
    }
}
