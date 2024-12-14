package fr.xgouchet.luxels.components.render.projection

import fr.xgouchet.luxels.components.render.SinglePositionProjection
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.xy
import fr.xgouchet.luxels.engine.render.Projection

/**
 * A [Projection] using an orthographic 2D camera.
 *
 * @param simulationVolume the simulation volume
 * @param filmSpace the film space
 */
class FlatXYProjection(
    simulationVolume: Volume<Dimension.D3>,
    filmSpace: Volume<Dimension.D2>,
) : SinglePositionProjection<Dimension.D3>(simulationVolume, filmSpace) {

    // region Projection

    override fun project(position: Vector<Dimension.D3>): List<Vector<Dimension.D2>> {
        val scaledPosition = (position - simulationVolume.min) / simulationVolume.size
        return listOf((scaledPosition.xy * filmSpace.size) + filmSpace.min)
    }

    // endregion
}
