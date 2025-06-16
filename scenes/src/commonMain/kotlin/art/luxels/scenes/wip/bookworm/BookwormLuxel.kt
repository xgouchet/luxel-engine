package art.luxels.scenes.wip.bookworm

import art.luxels.components.color.WLVaryingColorSource
import art.luxels.components.color.WaveLengthNanometer
import art.luxels.components.engine.PrincipledLuxel
import art.luxels.components.position.SimplePositionSource
import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.math.random.RandomGenerator
import art.luxels.core.model.AgeingLifespanSource
import art.luxels.core.position.PositionSource
import art.luxels.imageio.color.HDRColor

class BookwormLuxel(
    val rng: RandomGenerator<Vector<Dimension.D3>>,
    val sentence: BookwormSentence,
    waveLength: WaveLengthNanometer,
    lifespan: Int,
) : PrincipledLuxel<Dimension.D3, WLVaryingColorSource, PositionSource<Dimension.D3>, AgeingLifespanSource>(
    WLVaryingColorSource(waveLength),
    SimplePositionSource(Vector.unit(Dimension.D3)),
    AgeingLifespanSource(lifespan),
) {

    // region Luxel

    override fun position(): Vector<Dimension.D3> {
        return sentence.getPosition(lifespanSource.progression) + (rng.gaussian() * 0.00025)
    }

    override fun color(): HDRColor {
        colorSource.intensity = sentence.getIntensity(lifespanSource.progression)
        return colorSource.color()
    }

    // endregion
}
