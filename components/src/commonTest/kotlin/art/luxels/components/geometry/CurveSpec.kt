package art.luxels.components.geometry

import art.luxels.core.math.Dimension
import io.kotest.core.spec.style.DescribeSpec

class CurveSpec : DescribeSpec(
    {
        include(abstractCurveSpec(Dimension.D1))
        include(abstractCurveSpec(Dimension.D2))
        include(abstractCurveSpec(Dimension.D3))
        include(abstractCurveSpec(Dimension.D4))
    },
)
