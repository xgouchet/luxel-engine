package fr.xgouchet.luxels.cli.rain

import fr.xgouchet.luxels.components.color.EMSColorSource
import fr.xgouchet.luxels.components.position.InertiaPositionSource
import fr.xgouchet.luxels.core.math.geometry.Vector3
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.PrincipledLuxel

internal class RainLuxel(
    lifespan: Int,
    waveLength: Double,
    initialPosition: Vector3,
    initialSpeed: Vector3,
    var refractionIndex: Double,
) : PrincipledLuxel<EMSColorSource, InertiaPositionSource, AgeingLifespanSource>(
    EMSColorSource(waveLength),
    InertiaPositionSource(initialPosition, initialSpeed),
    AgeingLifespanSource(lifespan),
) {
    val reductionFactor = (10000.0 - ((waveLength - EMSColorSource.MIN_UV_LIGHT) * 10.0)) / 10000.0
}
