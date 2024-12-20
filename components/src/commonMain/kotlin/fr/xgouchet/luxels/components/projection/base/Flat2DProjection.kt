package fr.xgouchet.luxels.components.projection.base

import fr.xgouchet.luxels.components.projection.SinglePositionProjection
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.engine.render.Projection

/**
 * A basic 2D [Projection].
 *
 * @param simulationVolume the simulation volume
 * @param filmSpace the film space
 */
class Flat2DProjection(
    simulationVolume: Volume<Dimension.D2>,
    filmSpace: Volume<Dimension.D2>,
) : SinglePositionProjection<Dimension.D2>(simulationVolume, filmSpace) {

    // region Projection

    override fun project(position: Vector<Dimension.D2>): List<Vector<Dimension.D2>> {
        val scaledPosition = (position - simulationVolume.min) / simulationVolume.size
        return listOf((scaledPosition * filmSpace.size) + filmSpace.min)
    }

    // endregion
}
