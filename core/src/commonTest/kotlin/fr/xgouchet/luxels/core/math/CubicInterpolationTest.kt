package fr.xgouchet.luxels.core.math

import io.kotest.core.spec.style.DescribeSpec

class CubicInterpolationTest : DescribeSpec({
    include(AbstractInterpolationTest(Interpolation.Cubic))
})
