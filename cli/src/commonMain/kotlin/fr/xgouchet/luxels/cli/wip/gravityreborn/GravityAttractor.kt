package fr.xgouchet.luxels.cli.wip.gravityreborn

import fr.xgouchet.luxels.components.animation.AnimatedVector
import fr.xgouchet.luxels.components.animation.inVolumeAnimated
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.fromSpherical
import fr.xgouchet.luxels.core.math.random.RndGen
import kotlin.math.PI
import kotlin.time.Duration

data class GravityAttractor(
    val gravity: Double,
    val orbit: Double,
    val position: AnimatedVector<D3>,
    val axis: Vector<D3>,
) {
    constructor(
        volume: Volume<D3>,
        duration: Duration,
        basePosition: Vector<D3>,
    ) : this(
        gravity = RndGen.double.inRange(5.0, 15.0),
        orbit = RndGen.double.inRange(5.0, 15.0),
        position = RndGen.vector3.inVolumeAnimated(
            Volume(
                basePosition - (volume.size / 3.0),
                basePosition + (volume.size / 3.0),
            ),
            duration,
        ),
        axis = fromSpherical(
            RndGen.double.inRange(0.0, TAU),
            RndGen.double.inRange(0.0, PI),
            1.0,
        ),
    )
}
