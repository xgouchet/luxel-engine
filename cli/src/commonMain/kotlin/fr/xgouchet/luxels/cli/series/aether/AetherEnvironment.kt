package fr.xgouchet.luxels.cli.series.aether

import fr.xgouchet.luxels.components.engine.BaseEnvironment
import fr.xgouchet.luxels.components.geometry.Curve
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.random.inVolume

class AetherEnvironment(
    simulationVolume: Volume<D3>,
) : BaseEnvironment<D3>(simulationVolume) {

    private val curves = List(RndGen.int.inRange(5, 9)) {
        Curve(List(RndGen.int.inRange(4, 8)) { RndGen.vector3.inVolume(simulationVolume) })
    }

    fun getCurve(t: Double): Curve<D3> {
        return Curve(curves.map { it.getPosition(t) })
    }
}
