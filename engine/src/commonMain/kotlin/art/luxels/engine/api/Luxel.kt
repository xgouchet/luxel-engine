package art.luxels.engine.api

import art.luxels.core.color.ColorSource
import art.luxels.core.math.Dimension
import art.luxels.core.model.LifespanSource
import art.luxels.core.position.PositionSource

/**
 * A basic Luxel representation.
 * @param D the dimension of the space luxels evolve in
 */
interface Luxel<D : Dimension> :
    ColorSource,
    PositionSource<D>,
    LifespanSource
