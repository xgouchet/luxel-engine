package fr.xgouchet.luxels.cli.fractals.buddhabrot

import fr.xgouchet.luxels.components.engine.PrincipledLuxel
import fr.xgouchet.luxels.core.color.ColorSource
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.model.AgeingLifespanSource

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
