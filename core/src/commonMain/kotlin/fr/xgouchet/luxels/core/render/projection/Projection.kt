package fr.xgouchet.luxels.core.render.projection

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.exposure.Film

/**
 * A Projection converts the position of a [Luxel] in simuletion into a position on a [Film].
 */
interface Projection<D : Dimension> {

    /** The simulation space. */
    abstract val simulationSpace: Volume<D>

    /** The film space. */
    abstract val filmSpace: Volume<Dimension.D2>

    /**
     * Converts a simulation space position to a film space position.
     * @param position a simulation-space position
     * @return the converted film-space position
     */
    fun convertPosition(position: Vector<D>): Vector<Dimension.D2>
}
