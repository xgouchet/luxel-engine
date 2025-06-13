package art.luxels.components.projection.base

import art.luxels.components.projection.SinglePositionProjection
import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.math.Volume
import art.luxels.core.math.xz
import art.luxels.engine.render.Projection

/**
 * A [Projection] using an orthographic 2D camera.
 * @param simulationVolume the simulation volume
 * @param filmSpace the film space
 */
class FlatXZProjection(simulationVolume: Volume<Dimension.D3>, filmSpace: Volume<Dimension.D2>) :
    SinglePositionProjection<Dimension.D3>(simulationVolume, filmSpace) {

    // region Projection

    override fun project(position: Vector<Dimension.D3>): List<Vector<Dimension.D2>> {
        val scaledPosition = (position - simulationVolume.min) / simulationVolume.size
        return listOf((scaledPosition.xz * filmSpace.size) + filmSpace.min)
    }

    // endregion
}
