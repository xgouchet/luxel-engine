package art.luxels.components.engine

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.math.Volume
import art.luxels.engine.api.Environment
import art.luxels.imageio.color.HDRColor
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
