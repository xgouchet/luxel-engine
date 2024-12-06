package fr.xgouchet.luxels.components.geometry

import fr.xgouchet.luxels.core.math.Dimension
import io.kotest.core.spec.style.DescribeSpec

class CurveSpec : DescribeSpec(
    {
        include(abstractCurveSpec(Dimension.D1))
        include(abstractCurveSpec(Dimension.D2))
        include(abstractCurveSpec(Dimension.D3))
        include(abstractCurveSpec(Dimension.D4))
    },
)
