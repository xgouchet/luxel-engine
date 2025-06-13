package art.luxels.scenes.wip.gravityreborn

import art.luxels.components.engine.BaseEnvironment
import art.luxels.core.math.Dimension.D3
import art.luxels.core.math.Vector
import art.luxels.core.math.Volume
import art.luxels.imageio.color.HDRColor
import kotlin.time.Duration

class GravityEnvironment(simulationVolume: Volume<D3>, duration: Duration) : BaseEnvironment<D3>(simulationVolume) {

    val attractors = List(7) { GravityAttractor(simulationVolume, duration, simulationVolume.corner(it)) }

    // region Environment

    override fun environmentColor(position: Vector<D3>, time: Duration): HDRColor {
        return attractors.fold(HDRColor.TRANSPARENT) { prev, it ->
            val distance = (it.position.getValue(time) - position).squaredLength()

            val baseColor = HDRColor.RED + (HDRColor.BLUE * it.gravity) + (HDRColor.GREEN * it.orbit)

            prev + (baseColor / (distance + 10.0))
        }
    }

    // endregion
}
