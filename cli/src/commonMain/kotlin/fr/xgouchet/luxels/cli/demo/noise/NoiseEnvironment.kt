package fr.xgouchet.luxels.cli.demo.noise

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.engine.BaseEnvironment
import fr.xgouchet.luxels.components.noise.FractalNoiseGenerator.Companion.brownNoise
import fr.xgouchet.luxels.components.noise.NoiseGenerator
import fr.xgouchet.luxels.components.noise.PerlinNoiseGenerator
import fr.xgouchet.luxels.components.noise.wrapper.VectorToDoubleNoiseGenerator
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Volume
import kotlin.time.Duration

class NoiseEnvironment(simulationVolume: Volume<D2>) : BaseEnvironment<D2>(simulationVolume) {

    private val generator: NoiseGenerator<Vector<D2>, Double> by lazy {
        VectorToDoubleNoiseGenerator(
            brownNoise(PerlinNoiseGenerator()),
        )
    }

    private val noiseScale = Vector2(0.001, 0.001)

    fun getNoise(position: Vector<D2>): Double {
        return generator.noise(position * noiseScale)
    }

    // region Environment

    override fun environmentColor(position: Vector<D2>, time: Duration): HDRColor {
        TODO("Not yet implemented")
    }

    // endregion
}
