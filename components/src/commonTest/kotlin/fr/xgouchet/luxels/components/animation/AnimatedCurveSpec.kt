package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.core.math.Dimension
import io.kotest.core.spec.style.DescribeSpec

class AnimatedCurveSpec : DescribeSpec(
    {
        include(abstractAnimatedCurveSpec(Dimension.D1))
        include(abstractAnimatedCurveSpec(Dimension.D2))
        include(abstractAnimatedCurveSpec(Dimension.D3))
        include(abstractAnimatedCurveSpec(Dimension.D4))
    },
)
