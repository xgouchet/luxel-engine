package art.luxels.cli.wip.bookworm

import art.luxels.components.color.EMSColorSource
import art.luxels.components.color.WavelengthNanometer
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
    wavelength: WavelengthNanometer,
    lifespan: Int,
) : PrincipledLuxel<Dimension.D3, EMSColorSource, PositionSource<Dimension.D3>, AgeingLifespanSource>(
    EMSColorSource(wavelength),
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
