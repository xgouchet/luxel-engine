package fr.xgouchet.luxels.components.render

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.engine.render.Projection

/**
 * A [Projection] implementation that returns a single position .
 *
 * @param D the dimension of the source dimension
 *
 * @param simulationVolume the simulation volume
 * @param filmSpace the film space
 */
abstract class SinglePositionProjection<D : Dimension>(
    simulationVolume: Volume<D>,
    filmSpace: Volume<Dimension.D2>,
) : AbstractProjection<D>(simulationVolume, filmSpace) {

    // region Projection

    final override fun project(position: Vector<D>, color: Color): List<Pair<Vector<Dimension.D2>, Color>> {
        return project(position).map { vector -> vector to color }
    }

    // endregion

    /**
     * Projects the given simulation space position onto film space.
     * @param position the position in simulation coordinates
     * @return the list of projected positions in film space
     */
    abstract fun project(position: Vector<D>): List<Vector<Dimension.D2>>
}
