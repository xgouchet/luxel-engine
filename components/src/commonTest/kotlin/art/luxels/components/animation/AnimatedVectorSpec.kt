package art.luxels.components.animation

import art.luxels.core.math.Dimension
import io.kotest.core.spec.style.DescribeSpec

class AnimatedVectorSpec : DescribeSpec(
    {
        include(abstractAnimatedVectorSpec(Dimension.D1))
        include(abstractAnimatedVectorSpec(Dimension.D2))
        include(abstractAnimatedVectorSpec(Dimension.D3))
        include(abstractAnimatedVectorSpec(Dimension.D4))
    },
)
