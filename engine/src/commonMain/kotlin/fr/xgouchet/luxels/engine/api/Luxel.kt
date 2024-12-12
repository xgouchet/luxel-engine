package fr.xgouchet.luxels.engine.api

import fr.xgouchet.luxels.core.color.ColorSource
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.model.LifespanSource
import fr.xgouchet.luxels.core.position.PositionSource

/**
 * A basic Luxel representation.
 * @param D the dimension of the space luxels evolve in
 */
interface Luxel<D : Dimension> : ColorSource, PositionSource<D>, LifespanSource
