package fr.xgouchet.luxels.cli.series.s03gravity

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.engine.BaseEnvironment
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.random.RndGen
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
