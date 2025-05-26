package art.luxels.scenes.demo.noise

import art.luxels.components.engine.BaseEnvironment
import art.luxels.components.noise.FractalNoiseGenerator.Companion.brownNoise
import art.luxels.components.noise.NoiseGenerator
import art.luxels.components.noise.PerlinNoiseGenerator
import art.luxels.components.noise.wrapper.VectorToDoubleNoiseGenerator
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.Vector
import art.luxels.core.math.Vector2
import art.luxels.core.math.Volume
import art.luxels.imageio.color.HDRColor
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
