package fr.xgouchet.luxels.core.render.projection

import fr.xgouchet.luxels.core.math.geometry.Space2
import fr.xgouchet.luxels.core.math.geometry.Space3
import fr.xgouchet.luxels.core.math.geometry.Vector2
import fr.xgouchet.luxels.core.math.geometry.Vector3

/**
 * A [Projection] using an orthographic 2D camera.
 * @property simulationSpace the simulation space
 * @property filmSpace the film space
 */
internal class FlatXYProjection(
    override val simulationSpace: Space3,
    override val filmSpace: Space2,
) : Projection {
    // region Projection

    override fun convertPosition(position: Vector3): Vector2 {
        val scaledPosition = (position - simulationSpace.min) / simulationSpace.size
        return ((scaledPosition.xy * filmSpace.size) + filmSpace.min)
    }

    // endregion
}
