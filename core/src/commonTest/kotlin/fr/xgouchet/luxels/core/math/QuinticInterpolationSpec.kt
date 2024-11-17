package fr.xgouchet.luxels.core.math

import io.kotest.core.spec.style.DescribeSpec

class QuinticInterpolationSpec : DescribeSpec({
    include(abstractInterpolationSpec(Interpolation.Quintic))
})
