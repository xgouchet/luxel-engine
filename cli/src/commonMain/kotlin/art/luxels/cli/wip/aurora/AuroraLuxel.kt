package art.luxels.cli.wip.aurora

import art.luxels.components.color.atomic.ASLColorSource
import art.luxels.components.engine.PrincipledLuxel
import art.luxels.components.position.FuzzyPositionSource
import art.luxels.components.position.InertiaPositionSource
import art.luxels.core.color.ColorSource
import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.math.Vector3
import art.luxels.core.model.AgeingLifespanSource
import art.luxels.imageio.color.HDRColor
import kotlin.math.max

class AuroraLuxel(
    element: ASLColorSource,
    initialPosition: Vector<Dimension.D3>,
    initialSpeed: Vector<Dimension.D3>,
    fuzzyRadius: Double,
    lifespan: Int,
    var intensity: Double = 0.0,
) : PrincipledLuxel<
        Dimension.D3,
        ColorSource,
        FuzzyPositionSource<Dimension.D3, InertiaPositionSource<Dimension.D3>>,
        AgeingLifespanSource,
        >(
    element,
    FuzzyPositionSource(Dimension.D3, InertiaPositionSource(initialPosition, initialSpeed), fuzzyRadius),
    AgeingLifespanSource(lifespan),
) {
    // region Luxel

    override fun color(): HDRColor {
//        val progression = lifespanSource.progression
//        val scale = if (progression < 0.01) 200.0 else 1.0 / progression
        val actualIntensity = max((intensity - 0.333) * 3.0, 0.0)
        return super.color() * actualIntensity
    }

    override fun onStep(step: Int) {
        positionSource.delegate.updateSpeed(Vector3(0.0, 1.0, 0.0), 100.0)
        super.onStep(step)
    }

    // endregion
}
