package art.luxels.scenes.wip.rain

import art.luxels.components.color.WLColorSource
import art.luxels.components.color.WLVaryingColorSource
import art.luxels.components.engine.PrincipledLuxel
import art.luxels.components.position.InertiaPositionSource
import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.model.AgeingLifespanSource

class RainLuxel(
    lifespan: Int,
    waveLength: Double,
    initialPosition: Vector<Dimension.D2>,
    initialSpeed: Vector<Dimension.D2>,
    var refractionIndex: Double,
) : PrincipledLuxel<Dimension.D2, WLVaryingColorSource, InertiaPositionSource<Dimension.D2>, AgeingLifespanSource>(
    WLVaryingColorSource(waveLength),
    InertiaPositionSource(initialPosition, initialSpeed),
    AgeingLifespanSource(lifespan),
) {
    val reductionFactor = (10000.0 - ((waveLength - WLColorSource.MIN_UV_LIGHT) * 10.0)) / 10000.0
}
