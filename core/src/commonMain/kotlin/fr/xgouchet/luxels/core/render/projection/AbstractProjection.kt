package fr.xgouchet.luxels.core.render.projection

import fr.xgouchet.luxels.core.position.Space2
import fr.xgouchet.luxels.core.position.Space3

abstract class AbstractProjection(
    override val simulationSpace: Space3,
    override val filmSpace: Space2,
) : Projection
