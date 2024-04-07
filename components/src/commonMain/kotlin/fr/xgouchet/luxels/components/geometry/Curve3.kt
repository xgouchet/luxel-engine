package fr.xgouchet.luxels.components.geometry

import fr.xgouchet.luxels.core.math.geometry.Vector3

/**
 * A [Curve] in 3D Space.
 * @param points the control points for the curve
 */
class Curve3(points: List<Vector3>) : Curve<Vector3>(points, Vector3.Builder) {
    constructor(vararg points: Vector3) : this(listOf(*points))
}
