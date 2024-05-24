package fr.xgouchet.luxels.core.render.projection

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.xy

/**
 * A [Projection] using an orthographic 2D camera.
 * @property simulationSpace the simulation space
 * @property filmSpace the film space
 */
internal class FlatXYProjection(
    override val simulationSpace: Volume<Dimension.D3>,
    override val filmSpace: Volume<Dimension.D2>,
) : Projection<Dimension.D3> {

    // region Projection

    override fun convertPosition(position: Vector<Dimension.D3>): Vector<Dimension.D2> {
        val scaledPosition = (position - simulationSpace.min) / simulationSpace.size
        return ((scaledPosition.xy * filmSpace.size) + filmSpace.min)
    }

    // endregion
}
