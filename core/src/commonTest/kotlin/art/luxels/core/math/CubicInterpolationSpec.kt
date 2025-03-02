package art.luxels.core.math

import io.kotest.core.spec.style.DescribeSpec

class CubicInterpolationSpec : DescribeSpec({
    include(abstractInterpolationSpec(Interpolation.Cubic))
})
