package fr.xgouchet.luxels.cli.aurora

import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.color.ColorSource
import fr.xgouchet.luxels.core.color.atomic.ASLColorSource
import fr.xgouchet.luxels.core.gen.random.RndGen
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.PrincipledLuxel
import fr.xgouchet.luxels.core.position.FuzzyPositionSource
import fr.xgouchet.luxels.core.position.InertiaPositionSource

class AuroraLuxel(
    element: ASLColorSource,
    initialPosition: Vector3,
    lifespan: Int,
) : PrincipledLuxel<ColorSource, FuzzyPositionSource<InertiaPositionSource>, AgeingLifespanSource>(
    element,
    FuzzyPositionSource(InertiaPositionSource(initialPosition, Vector3(0.0, -1.0, 0.0)), 10.0),
    AgeingLifespanSource(lifespan),
) {

    override fun color(): Color {
        val scale = 15.0 / (lifespanSource.age + 1 + RndGen.double.inRange(0.0, 1024.0))
        return super.color() * scale * scale
    }

    override fun onStep(step: Int) {
        positionSource.radius = ((RndGen.double.gaussian() * .1) + 1.0 - lifespanSource.progression) * 10.0
        super.onStep(step)
    }
}
