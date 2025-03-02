package art.luxels.cli.series.s03gravity

import art.luxels.components.animation.AnimatedVector
import art.luxels.components.animation.inVolumeAnimated
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.Volume
import art.luxels.core.math.random.RndGen
import kotlin.math.pow
import kotlin.time.Duration

data class GravityAttractor(
    val gravity: Double,
    val orbit: Double,
    val position: AnimatedVector<D2>,
) {
    constructor(
        volume: Volume<D2>,
        duration: Duration,
    ) : this(
        gravity = RndGen.double.inRange(5.0, 30.0),
        orbit = RndGen.double.inRange(5.0, 30.0) * (-1.0).pow(RndGen.int.uniform()),
        position = RndGen.vector2.inVolumeAnimated(volume, duration),
    )
}
