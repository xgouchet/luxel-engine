package fr.xgouchet.luxels.components.geometry

import fr.xgouchet.luxels.core.math.geometry.Vector4

/**
 * A [Curve] in 4D Space.
 * @param points the control points for the curve
 */
class Curve4(points: List<Vector4>) : Curve<Vector4>(points, Vector4.Builder) {
    constructor(vararg points: Vector4) : this(listOf(*points))
}
