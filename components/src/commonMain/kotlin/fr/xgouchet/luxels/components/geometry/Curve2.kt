package fr.xgouchet.luxels.components.geometry

import fr.xgouchet.luxels.core.math.geometry.Vector2

/**
 * A [Curve] in 2D Space.
 * @param points the control points for the curve
 */
class Curve2(points: List<Vector2>) : Curve<Vector2>(points, Vector2.Builder) {
    constructor(vararg points: Vector2) : this(listOf(*points))
}
