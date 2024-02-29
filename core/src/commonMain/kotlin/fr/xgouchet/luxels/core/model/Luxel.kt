package fr.xgouchet.luxels.core.model

import fr.xgouchet.luxels.core.color.ColorSource
import fr.xgouchet.luxels.core.position.PositionSource

/**
 * A basic Luxel representation.
 */
interface Luxel : ColorSource, PositionSource, LifespanSource
