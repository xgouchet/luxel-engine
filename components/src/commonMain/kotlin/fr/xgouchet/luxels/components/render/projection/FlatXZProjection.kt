package fr.xgouchet.luxels.components.render.projection

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.xz
import fr.xgouchet.luxels.core.render.projection.Projection

/**
 * A [Projection] using an orthographic 2D camera.
 * @property simulationSpace the simulation space
 * @property filmSpace the film space
 */
class FlatXZProjection(
    override val simulationSpace: Volume<Dimension.D3>,
    override val filmSpace: Volume<Dimension.D2>,
) : Projection<Dimension.D3> {

    // region Projection

    override fun convertPosition(position: Vector<Dimension.D3>): Vector<Dimension.D2> {
        val scaledPosition = (position - simulationSpace.min) / simulationSpace.size
        return ((scaledPosition.xz * filmSpace.size) + filmSpace.min)
    }

    // endregion
}
