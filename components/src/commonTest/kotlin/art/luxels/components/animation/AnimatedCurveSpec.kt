package art.luxels.components.animation

import art.luxels.core.math.Dimension
import io.kotest.core.spec.style.DescribeSpec

class AnimatedCurveSpec : DescribeSpec(
    {
        include(abstractAnimatedCurveSpec(Dimension.D1))
        include(abstractAnimatedCurveSpec(Dimension.D2))
        include(abstractAnimatedCurveSpec(Dimension.D3))
        include(abstractAnimatedCurveSpec(Dimension.D4))
    },
)
