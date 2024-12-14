package fr.xgouchet.luxels.components.engine

import fr.xgouchet.luxels.core.color.ColorSource
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.model.LifespanSource
import fr.xgouchet.luxels.core.position.PositionSource
import fr.xgouchet.luxels.engine.api.Luxel

/**
 * A generic [Luxel] implementation allowing specifying the [ColorSource], [PositionSource] and
 * [LifespanSource] independently.
 * @param D the dimension of the space luxels evolve in
 * @param C the type of [ColorSource]
 * @param P the type of [PositionSource]
 * @param L the type of [LifespanSource]
 * @property colorSource the [ColorSource] implementation of the luxel
 * @property positionSource the [PositionSource] implementation of the luxel
 * @property lifespanSource the [LifespanSource] implementation of the luxel
 */
open class PrincipledLuxel<D : Dimension, C : ColorSource, P : PositionSource<D>, L : LifespanSource>(
    val colorSource: C,
    val positionSource: P,
    val lifespanSource: L,
) :
    ColorSource by colorSource,
    PositionSource<D> by positionSource,
    LifespanSource by lifespanSource,
    Luxel<D>
