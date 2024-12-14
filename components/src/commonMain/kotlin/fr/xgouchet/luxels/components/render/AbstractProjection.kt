package fr.xgouchet.luxels.components.render

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.engine.render.Projection

/**
 * An abstract projection that uses the source (simulation) volume and target (film) space.
 *
 * @param D the dimension of the source dimension
 *
 * @property simulationVolume the simulation volume
 * @property filmSpace the film space
 */
abstract class AbstractProjection<D : Dimension>(
    val simulationVolume: Volume<D>,
    val filmSpace: Volume<D2>,
) : Projection<D>
