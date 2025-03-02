package art.luxels.cli.series.s03gravity

import art.luxels.components.engine.BaseEnvironment
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.Vector
import art.luxels.core.math.Volume
import art.luxels.core.math.random.RndGen
import art.luxels.imageio.color.HDRColor
import kotlin.time.Duration

class GravityEnvironment(
    simulationVolume: Volume<D2>,
    duration: Duration,
) : BaseEnvironment<D2>(simulationVolume) {

    val attractors = List(RndGen.int.inRange(7, 18)) { GravityAttractor(simulationVolume, duration) }

    // region Environment

    override fun environmentColor(position: Vector<D2>, time: Duration): HDRColor {
        TODO("Not yet implemented")
    }

    // endregion
}
