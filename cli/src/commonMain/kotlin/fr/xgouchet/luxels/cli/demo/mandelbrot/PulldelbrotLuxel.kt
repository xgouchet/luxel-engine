package fr.xgouchet.luxels.cli.demo.mandelbrot

import fr.xgouchet.luxels.core.color.ColorSource
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.PrincipledLuxel

internal class PulldelbrotLuxel(
    lifespan: Int,
    colorSource: ColorSource,
    positionSource: PulldelbrotPositionSource,
) : PrincipledLuxel<Dimension.D2, ColorSource, PulldelbrotPositionSource, AgeingLifespanSource>(
    colorSource,
    positionSource,
    AgeingLifespanSource(lifespan),
)
