package fr.xgouchet.luxels.cli.aurora

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.color.atomic.ASLColorSource
import fr.xgouchet.luxels.components.position.FuzzyPositionSource
import fr.xgouchet.luxels.components.position.InertiaPositionSource
import fr.xgouchet.luxels.core.color.ColorSource
import fr.xgouchet.luxels.core.math.geometry.Vector3
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.PrincipledLuxel
import kotlin.math.max

internal class AuroraLuxel(
    element: ASLColorSource,
    initialPosition: Vector3,
    initialSpeed: Vector3,
    fuzzyRadius: Double,
    lifespan: Int,
    var intensity: Double = 0.0,
) : PrincipledLuxel<ColorSource, FuzzyPositionSource<InertiaPositionSource>, AgeingLifespanSource>(
    element,
    FuzzyPositionSource(InertiaPositionSource(initialPosition, initialSpeed), fuzzyRadius),
    AgeingLifespanSource(lifespan),
) {
    // region Luxel

    override fun color(): HDRColor {
        val progression = lifespanSource.progression
//        val scale = if (progression < 0.01) 200.0 else 1.0 / progression
        val actualIntensity = max((intensity - 0.333) * 3.0, 0.0)
        return super.color() * actualIntensity
    }

    override fun onStep(step: Int) {
        positionSource.delegate.updateSpeed(Vector3.Y_AXIS, 100.0)
        super.onStep(step)
    }

    // endregion
}
