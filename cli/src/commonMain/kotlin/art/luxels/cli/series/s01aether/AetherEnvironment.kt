package art.luxels.cli.series.s01aether

import art.luxels.components.engine.BaseEnvironment
import art.luxels.components.geometry.Curve
import art.luxels.core.math.Dimension.D3
import art.luxels.core.math.Volume
import art.luxels.core.math.random.RndGen
import art.luxels.core.math.random.inVolume

class AetherEnvironment(
    simulationVolume: Volume<D3>,
) : BaseEnvironment<D3>(simulationVolume) {

    private val curves = List(RndGen.int.inRange(4, 8)) {
        Curve(List(RndGen.int.inRange(4, 8)) { RndGen.vector3.inVolume(simulationVolume) })
    }

    fun getCurve(t: Double): Curve<D3> {
        return Curve(curves.map { it.getPosition(t) })
    }
}
