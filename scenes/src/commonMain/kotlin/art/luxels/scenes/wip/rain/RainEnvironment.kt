package art.luxels.scenes.wip.rain

import art.luxels.components.color.EMSColorSource
import art.luxels.components.noise.FractalNoiseGenerator.Companion.whiteNoise
import art.luxels.components.noise.PerlinNoiseGenerator
import art.luxels.components.noise.wrapper.VectorNoiseGenerator
import art.luxels.core.math.Dimension.D1
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.TAU
import art.luxels.core.math.Vector
import art.luxels.core.math.Vector2
import art.luxels.core.math.Volume
import art.luxels.core.math.fromCircular
import art.luxels.core.math.random.RndGen
import art.luxels.core.math.random.inVolume
import art.luxels.core.math.x
import art.luxels.engine.api.Environment
import art.luxels.imageio.color.HDRColor
import kotlin.math.min
import kotlin.time.Duration

class RainEnvironment(val simulationVolume: Volume<D2>) : Environment<D2> {

    private val noiseField = VectorNoiseGenerator<D2, D1>(
        whiteNoise(PerlinNoiseGenerator(), 3),
        D1,
    )

    internal val extendedRange: Volume<D2> = simulationVolume.expanded(2.0)
    internal val safeRange: Volume<D2> = simulationVolume.expanded(1.2)

    private val noiseOffset: Vector<D2> = RndGen.vector2.inVolume(noiseOffsetVolume)
    private val noiseScale: Double = 1.0 / RndGen.double.inRange(100.0, 500.0)
    private val noiseThreshold: Double = RndGen.double.inRange(0.4, 0.6)

    private val animationDirection: Vector<D2> = Vector.nul(D2)
    private val animationOffset: Vector<D2> = Vector.nul(D2)

    private val inRI: Double = RndGen.double.inRange(1.0, 2.0)
    private val outRI: Double = RndGen.double.inRange(1.0, 2.0)
    internal var bounceThreshold: Double = RndGen.double.inRange(75.0, 100.0)

    internal val directionalRainSpeed: Vector<D2> = Vector2(RndGen.double.inRange(-2.0, 2.0), 1.0).normalized()

    internal val lightWaveLengthCenter =
        RndGen.double.inRange(EMSColorSource.MEAN_CYAN, EMSColorSource.MEAN_DARK_ORANGE)
    internal val lightWaveLengthWidth = min(
        lightWaveLengthCenter - EMSColorSource.MIN_VISIBLE_LIGHT,
        EMSColorSource.MAX_VISIBLE_LIGHT - lightWaveLengthCenter,
    )

    // region Environment

    override fun environmentColor(position: Vector<D2>, time: Duration): HDRColor {
        val ri = getRefractionIndex(position)
        return when (ri) {
            outRI -> HDRColor.BLUE
            inRI -> HDRColor.GREEN
            else -> HDRColor.RED
        }
    }

    // endregion

    // region Internal

    internal fun getRefractionIndex(position: Vector<D2>): Double {
        val offset = position + noiseOffset + animationOffset
        val scale = offset * noiseScale
        val noise = noiseField.noise(scale).x
        return if (noise > noiseThreshold) outRI else inRI
    }

    internal fun getNormal(position: Vector<D2>): Vector<D2> {
        var normal = Vector.nul(D2)
        val ri = getRefractionIndex(position)
        for (i in 0..<360 step 60) {
            val angle = TAU * i / 360.0
            val offset = fromCircular(angle, 10.0)
            val otherRi = getRefractionIndex(position + offset)
            if (otherRi != ri) {
                normal -= offset
            }
        }
        return normal.normalized()
    }

    // endregion

    companion object {
        val noiseOffsetVolume = Volume(
            Vector2(-65536.0, -65536.0),
            Vector2(65536.0, 65536.0),
        )

        val gravity = Vector2(0.0, 9.81)
    }
}
