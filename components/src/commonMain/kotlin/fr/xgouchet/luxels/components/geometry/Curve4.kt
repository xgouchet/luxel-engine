package fr.xgouchet.luxels.components.geometry

import fr.xgouchet.luxels.core.math.geometry.Vector4

class Curve4(points: List<Vector4>) : Curve<Vector4>(points) {

    constructor(vararg points: Vector4) : this(listOf(*points))

    // region Curve

    override fun getPointFromComponents(components: List<Double>): Vector4 {
        return Vector4.fromComponents(components)
    }

    // endregion
}
