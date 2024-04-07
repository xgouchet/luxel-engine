package fr.xgouchet.luxels.components.geometry

import fr.xgouchet.luxels.core.math.geometry.Vector3

class Curve3(points: List<Vector3>) : Curve<Vector3>(points) {

    constructor(vararg points: Vector3) : this(listOf(*points))

    // region Curve

    override fun getPointFromComponents(components: List<Double>): Vector3 {
        return Vector3.fromComponents(components)
    }

    // endregion
}
