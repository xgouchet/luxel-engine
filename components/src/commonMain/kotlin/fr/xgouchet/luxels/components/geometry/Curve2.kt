package fr.xgouchet.luxels.components.geometry

import fr.xgouchet.luxels.core.math.geometry.Vector2

class Curve2(points: List<Vector2>) : Curve<Vector2>(points) {

    constructor(vararg points: Vector2) : this(listOf(*points))

    // region Curve

    override fun getPointFromComponents(components: List<Double>): Vector2 {
        return Vector2.fromComponents(components)
    }

    // endregion
}
