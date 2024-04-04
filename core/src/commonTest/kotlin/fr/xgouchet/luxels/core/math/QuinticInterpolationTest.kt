package fr.xgouchet.luxels.core.math

import io.kotest.core.spec.style.DescribeSpec

class QuinticInterpolationTest : DescribeSpec({
    include(abstractInterpolationTest(Interpolation.Quintic))
})
