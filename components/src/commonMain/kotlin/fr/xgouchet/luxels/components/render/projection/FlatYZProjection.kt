package fr.xgouchet.luxels.components.render.projection

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.yz
import fr.xgouchet.luxels.engine.render.Projection

/**
 * A [Projection] using an orthographic 2D camera.
 * @property simulationVolume the simulation space
 * @property filmSpace the film space
 */
class FlatYZProjection(
    override val simulationVolume: Volume<Dimension.D3>,
    override val filmSpace: Volume<Dimension.D2>,
) : Projection<Dimension.D3> {

    // region Projection

    override fun project(position: Vector<Dimension.D3>): List<Vector<Dimension.D2>> {
        val scaledPosition = (position - simulationVolume.min) / simulationVolume.size
        return listOf((scaledPosition.yz * filmSpace.size) + filmSpace.min)
    }

    // endregion
}
