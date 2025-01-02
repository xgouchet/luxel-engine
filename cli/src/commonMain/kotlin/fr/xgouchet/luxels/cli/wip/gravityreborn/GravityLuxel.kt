package fr.xgouchet.luxels.cli.wip.gravityreborn

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.random.oneOf
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.random.inVolume
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.LifespanSource
import fr.xgouchet.luxels.engine.api.Luxel

class GravityLuxel(
    val channel: HDRColor,
    var position: Vector<D3>,
    val lifespanSource: AgeingLifespanSource,
) : Luxel<D3>, LifespanSource by lifespanSource {

    constructor(
        volume: Volume<D3>,
        lifespan: Int,
    ) : this(
        RndGen.oneOf(HDRColor.RED, HDRColor.GREEN, HDRColor.BLUE),
        RndGen.vector3.inVolume(volume.expanded(3.0)),
        AgeingLifespanSource(lifespan),
    )

    val tint: HDRColor = HDRColor(1.0, 1.0, 1.0) * channel
    val deviationFactor = ((channel.r * 1) + (channel.g * 2) + (channel.b * 3))

    // region ColorSource

    override fun color(): HDRColor {
        return tint
    }

    // endregion

    // region PositionSource

    override fun position(): Vector<D3> {
        return position
    }

    // endregion
}
