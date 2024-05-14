package fr.xgouchet.luxels.core.math

import io.kotest.core.spec.style.DescribeSpec

class VectorSpec : DescribeSpec({

    include(abstractVectorSpec(Dimension.D1))
    include(abstractVectorSpec(Dimension.D2))
    include(abstractVectorSpec(Dimension.D3))
    include(abstractVectorSpec(Dimension.D4))
    include(abstractVectorSpec(Dimension.D5))
    include(abstractVectorSpec(Dimension.D6))
})
