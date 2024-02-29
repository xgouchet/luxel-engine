package fr.xgouchet.luxels.core.model

import fr.xgouchet.luxels.core.color.ColorSource
import fr.xgouchet.luxels.core.position.PositionSource

/**
 * A generic [Luxel] implementation allowing specifying the [ColorSource], [PositionSource] and
 * [LifespanSource] independently.
 * @param C the type of [ColorSource]
 * @param P the type of [PositionSource]
 * @param L the type of [LifespanSource]
 * @property colorSource the [ColorSource] implementation of the luxel
 * @property positionSource the [PositionSource] implementation of the luxel
 * @property lifespanSource the [LifespanSource] implementation of the luxel
 */
open class PrincipledLuxel<C : ColorSource, P : PositionSource, L : LifespanSource>(
    val colorSource: C,
    val positionSource: P,
    val lifespanSource: L,
) :
    ColorSource by colorSource,
    PositionSource by positionSource,
    LifespanSource by lifespanSource,
    Luxel
