package fr.xgouchet.luxels.components.render.projection

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.engine.render.Projection

/**
 * A basic 2D [Projection].
 * @property simulationVolume the simulation space
 * @property filmSpace the film space
 */
class Flat2DProjection(
    override val simulationVolume: Volume<Dimension.D2>,
    override val filmSpace: Volume<Dimension.D2>,
) : Projection<Dimension.D2> {

    // region Projection

    override fun project(position: Vector<Dimension.D2>): List<Vector<Dimension.D2>> {
        val scaledPosition = (position - simulationVolume.min) / simulationVolume.size
        return listOf((scaledPosition * filmSpace.size) + filmSpace.min)
    }

    // endregion
}
