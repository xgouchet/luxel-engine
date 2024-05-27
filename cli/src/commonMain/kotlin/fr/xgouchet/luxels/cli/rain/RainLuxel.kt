package fr.xgouchet.luxels.cli.rain

import fr.xgouchet.luxels.components.color.EMSColorSource
import fr.xgouchet.luxels.components.position.InertiaPositionSource
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.PrincipledLuxel

internal class RainLuxel(
    lifespan: Int,
    waveLength: Double,
    initialPosition: Vector<Dimension.D2>,
    initialSpeed: Vector<Dimension.D2>,
    var refractionIndex: Double,
) : PrincipledLuxel<Dimension.D2, EMSColorSource, InertiaPositionSource<Dimension.D2>, AgeingLifespanSource>(
    EMSColorSource(waveLength),
    InertiaPositionSource(initialPosition, initialSpeed),
    AgeingLifespanSource(lifespan),
) {
    val reductionFactor = (10000.0 - ((waveLength - EMSColorSource.MIN_UV_LIGHT) * 10.0)) / 10000.0
}
