package fr.xgouchet.luxels.components.engine

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.engine.api.Environment
import kotlin.time.Duration

/**
 * An empty environment for scenes that are purely rule-based.
 *
 * @param D the dimension of the space luxels evolve in
 *
 * @property simulationVolume the simulation volume
 */
open class BaseEnvironment<D : Dimension>(val simulationVolume: Volume<D>) : Environment<D> {

    // region Environment

    override fun environmentColor(position: Vector<D>, time: Duration): HDRColor {
        return HDRColor.TRANSPARENT
    }

    // endregion
}
