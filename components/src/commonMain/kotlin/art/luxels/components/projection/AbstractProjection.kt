package art.luxels.components.projection

import art.luxels.core.math.Dimension
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.Volume
import art.luxels.engine.render.Projection

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
