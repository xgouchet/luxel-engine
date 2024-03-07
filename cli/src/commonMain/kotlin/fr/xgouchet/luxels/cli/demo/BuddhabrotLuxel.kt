package fr.xgouchet.luxels.cli.demo

import fr.xgouchet.luxels.core.color.ColorSource
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.PrincipledLuxel

class BuddhabrotLuxel(
    lifespan: Int,
    colorSource: ColorSource,
    positionSource: BuddhabrotPositionSource,
) : PrincipledLuxel<ColorSource, BuddhabrotPositionSource, AgeingLifespanSource>(
    colorSource,
    positionSource,
    AgeingLifespanSource(lifespan),
) {

    override fun isAlive(): Boolean {
        return super.isAlive() && (positionSource.complexPosition.squaredLength() < 25.0)
    }
}
