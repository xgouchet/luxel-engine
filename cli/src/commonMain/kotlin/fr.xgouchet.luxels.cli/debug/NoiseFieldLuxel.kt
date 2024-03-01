package fr.xgouchet.luxels.cli.debug

import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.color.StaticColorSource
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.PrincipledLuxel
import fr.xgouchet.luxels.core.position.FuzzyPositionSource
import fr.xgouchet.luxels.core.position.StaticPositionSource

class NoiseFieldLuxel(
    initialPosition: Vector3,
    color: Color,
) : PrincipledLuxel<StaticColorSource, FuzzyPositionSource<StaticPositionSource>, AgeingLifespanSource>(
    StaticColorSource(color),
    FuzzyPositionSource(
//        InertiaPositionSource(initialPosition, Vector3.NULL),
        StaticPositionSource(initialPosition),
        100.0,
    ),
    AgeingLifespanSource(8),
)
