package fr.xgouchet.luxels.cli.bookworm

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.color.EMSColorSource
import fr.xgouchet.luxels.components.color.WavelengthNanometer
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.random.RandomGenerator
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.PrincipledLuxel
import fr.xgouchet.luxels.core.position.PositionSource
import fr.xgouchet.luxels.core.position.SimplePositionSource

internal class BookwormLuxel(
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
