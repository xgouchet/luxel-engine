package fr.xgouchet.luxels.core.render.projection

import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.position.Space2
import fr.xgouchet.luxels.core.position.Space3
import fr.xgouchet.luxels.core.render.exposure.Film

/**
 * A Projection converts the position of a [Luxel] into a 2D position on a [Film].
 */
interface Projection {
    /** The simulation space. */
    abstract val simulationSpace: Space3

    /** The film space. */
    abstract val filmSpace: Space2

    /**
     * Converts a 3D, simulation space position to a 2D film space position.
     * @param position a simulation-space position
     * @return the converted film-space position
     */
    fun convertPosition(position: Vector3): Vector2
}
