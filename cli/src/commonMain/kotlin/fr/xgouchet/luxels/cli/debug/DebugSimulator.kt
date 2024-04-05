package fr.xgouchet.luxels.cli.debug

import fr.xgouchet.luxels.components.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.components.noise.FractalNoiseGenerator
import fr.xgouchet.luxels.components.noise.PerlinNoiseGenerator
import fr.xgouchet.luxels.components.noise.PingPongNoiseGenerator
import fr.xgouchet.luxels.components.noise.wrapper.Vector3ToDoubleNoiseGenerator
import fr.xgouchet.luxels.components.noise.wrapper.Vector3ToVector3NoiseGenerator
import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.math.geometry.Vector3
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.random.inBox
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

internal class DebugSimulator : Simulator<DebugLuxel, Long> {

    val baseNoise: DimensionalNoiseGenerator = PerlinNoiseGenerator()
    val ridged = PingPongNoiseGenerator(baseNoise)
    val brownian = FractalNoiseGenerator(ridged, 4)
    val vectorToDouble = Vector3ToDoubleNoiseGenerator(brownian)
    val vectorToVector = Vector3ToVector3NoiseGenerator(brownian)

    private val noiseScale = Vector3(0.005, 0.005, 0.005)

    private var noiseOffset = Vector3.NULL

    // region Simulator

    override fun onFrameStart(simulation: Configuration.Simulation, time: Duration) {
        noiseOffset = Vector3(0.0, 0.0, time.inWholeMilliseconds * 0.001)
    }

    override fun spawnLuxel(simulation: Configuration.Simulation, time: Duration): DebugLuxel {
        val position = RndGen.vector3.inBox(simulation.space)
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

    // endregion
}
