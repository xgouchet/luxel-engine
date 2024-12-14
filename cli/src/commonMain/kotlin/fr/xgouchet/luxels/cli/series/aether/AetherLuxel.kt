package fr.xgouchet.luxels.cli.series.aether

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.color.EMSColorSource
import fr.xgouchet.luxels.components.color.WavelengthNanometer
import fr.xgouchet.luxels.components.engine.PrincipledLuxel
import fr.xgouchet.luxels.components.geometry.Curve
import fr.xgouchet.luxels.components.position.SimplePositionSource
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import kotlin.math.cos

class AetherLuxel(
    private val curve3: Curve<Dimension.D3>,
    waveLength: WavelengthNanometer,
    lifespan: Int,
) : PrincipledLuxel<Dimension.D3, EMSColorSource, SimplePositionSource<Dimension.D3>, AgeingLifespanSource>(
    EMSColorSource(waveLength),
    SimplePositionSource(Vector.nul(Dimension.D3)),
    AgeingLifespanSource(lifespan),
) {

    // region Luxel

    override fun position(): Vector<Dimension.D3> {
        val progress = lifespanSource.progression
        return curve3.getPosition(progress)
    }

    override fun color(): HDRColor {
        val rawColor = super.color()
        val progress = lifespanSource.progression
        val factor = (1.0 - cos(progress * TAU)) / 2.0

        return rawColor * factor
    }

    // endregion
}
