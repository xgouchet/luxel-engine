package art.luxels.cli.fractals.buddhabrot

import art.luxels.components.engine.PrincipledLuxel
import art.luxels.core.color.ColorSource
import art.luxels.core.math.Dimension
import art.luxels.core.model.AgeingLifespanSource

class BuddhabrotLuxel(
    lifespan: Int,
    colorSource: ColorSource,
    positionSource: BuddhabrotPositionSource,
) : PrincipledLuxel<Dimension.D2, ColorSource, BuddhabrotPositionSource, AgeingLifespanSource>(
    colorSource,
    positionSource,
    AgeingLifespanSource(lifespan),
) {
    // region Luxel

    override fun isAlive(): Boolean {
        return super.isAlive() && positionSource.isAlive()
    }

    // endregion
}
