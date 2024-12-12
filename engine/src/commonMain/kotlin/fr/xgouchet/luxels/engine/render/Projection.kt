package fr.xgouchet.luxels.engine.render

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.render.Film
import fr.xgouchet.luxels.engine.api.Luxel

/**
 * A Projection converts the position of a [Luxel] in simulation into a position on a [Film].
 */
interface Projection<D : Dimension> { // TODO make it even more generic?

    /** The simulation space. */
    val simulationVolume: Volume<D>

    /** The film space. */
    val filmSpace: Volume<Dimension.D2>

    /**
     * Converts a simulation space position to a film space position.
     * @param position a simulation-space position
     * @return the converted film-space position
     */
    fun project(position: Vector<D>): List<Vector<Dimension.D2>>
}
