package art.luxels.cli.series.s01aether

import art.luxels.components.color.WLStaticColorSource
import art.luxels.components.color.WaveLengthNanometer
import art.luxels.components.geometry.Curve
import art.luxels.core.color.ColorSource
import art.luxels.core.math.Dimension.D3
import art.luxels.core.math.TAU
import art.luxels.core.math.Vector
import art.luxels.core.model.AgeingLifespanSource
import art.luxels.core.model.LifespanSource
import art.luxels.engine.api.Luxel
import art.luxels.imageio.color.HDRColor
import kotlin.math.cos

class AetherLuxel(
    private val curve: Curve<D3>,
    val colorSource: ColorSource,
    val lifespanSource: AgeingLifespanSource,
) : Luxel<D3>,
    LifespanSource by lifespanSource {

    constructor(
        curve: Curve<D3>,
        waveLength: WaveLengthNanometer,
        lifespan: Int,
    ) : this(curve, WLStaticColorSource(waveLength), AgeingLifespanSource(lifespan))

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
