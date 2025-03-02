package art.luxels.cli.series.s03gravity

import art.luxels.components.random.oneOf
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.Vector
import art.luxels.core.math.Volume
import art.luxels.core.math.random.RndGen
import art.luxels.core.math.random.inVolume
import art.luxels.core.model.AgeingLifespanSource
import art.luxels.core.model.LifespanSource
import art.luxels.engine.api.Luxel
import art.luxels.imageio.color.HDRColor

class GravityLuxel(
    val channel: HDRColor,
    var position: Vector<D2>,
    val lifespanSource: AgeingLifespanSource,
) : Luxel<D2>, LifespanSource by lifespanSource {

    constructor(
        volume: Volume<D2>,
        lifespan: Int,
    ) : this(
        RndGen.oneOf(HDRColor.RED, HDRColor.GREEN, HDRColor.BLUE),
        RndGen.vector2.inVolume(volume),
        AgeingLifespanSource(lifespan),
    )

    val tint: HDRColor = HDRColor(0.25, 1.0, 4.0) * channel
    val deviationFactor = ((channel.r * 1) + (channel.g * 2) + (channel.b * 3))

    // region ColorSource

    override fun color(): HDRColor {
        return tint
    }

    // endregion

    // region PositionSource

    override fun position(): Vector<D2> {
        return position
    }

    // endregion
}
