package fr.xgouchet.luxels.core.render.projection

import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.position.Space2
import fr.xgouchet.luxels.core.position.Space3

internal class FlatXYProjection(
    private val simulationSpace: Space3,
    private val filmSpace: Space2,
) : Projection {

    // region Projection

    override fun convertPosition(position: Vector3): Vector2 {
        val scaledPosition = (position - simulationSpace.min) / simulationSpace.size
        return ((scaledPosition.xy * filmSpace.size) + filmSpace.min)
    }

    // endregion
}
