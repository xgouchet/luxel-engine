package fr.xgouchet.luxels.components.render.projection

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.render.projection.Projection

/**
 * A basic 2D [Projection].
 * @property simulationSpace the simulation space
 * @property filmSpace the film space
 */
class Flat2DProjection(
    override val simulationSpace: Volume<Dimension.D2>,
    override val filmSpace: Volume<Dimension.D2>,
) : Projection<Dimension.D2> {

    // region Projection

    override fun convertPosition(position: Vector<Dimension.D2>): Vector<Dimension.D2> {
        val scaledPosition = (position - simulationSpace.min) / simulationSpace.size
        return ((scaledPosition * filmSpace.size) + filmSpace.min)
    }

    // endregion
}
