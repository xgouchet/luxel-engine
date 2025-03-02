package art.luxels.cli.wip.rain

import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.Vector
import art.luxels.engine.api.Environment
import art.luxels.imageio.color.HDRColor
import kotlin.time.Duration

class RainEnvironment : Environment<D2> {
    // region Environment
    override fun environmentColor(
        position: Vector<D2>,
        time: Duration,
    ): HDRColor {
        TODO("Not yet implemented")
    }
    // endregion
}