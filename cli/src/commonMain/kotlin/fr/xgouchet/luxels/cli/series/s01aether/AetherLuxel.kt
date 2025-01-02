package fr.xgouchet.luxels.cli.series.s01aether

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.color.EMSColorSource
import fr.xgouchet.luxels.components.color.WavelengthNanometer
import fr.xgouchet.luxels.components.geometry.Curve
import fr.xgouchet.luxels.core.color.ColorSource
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.LifespanSource
import fr.xgouchet.luxels.engine.api.Luxel
import kotlin.math.cos

class AetherLuxel(
    private val curve: Curve<D3>,
    val colorSource: ColorSource,
    val lifespanSource: AgeingLifespanSource,
) : Luxel<D3>,
    LifespanSource by lifespanSource {

    constructor(
        curve: Curve<D3>,
        waveLength: WavelengthNanometer,
        lifespan: Int,
    ) : this(curve, EMSColorSource(waveLength), AgeingLifespanSource(lifespan))

    // region PositionSource

    override fun position(): Vector<D3> {
        val progress = lifespanSource.progression
        return curve.getPosition(progress)
    }

    // endregion

    // region ColorSource

    override fun color(): HDRColor {
        val rawColor = colorSource.color()
        val progress = lifespanSource.progression
        val factor = (1.0 - cos(progress * TAU)) / 2.0

        return rawColor * factor
    }

    // endregion
}
