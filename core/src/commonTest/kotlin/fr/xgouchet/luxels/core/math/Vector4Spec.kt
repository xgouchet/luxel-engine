package fr.xgouchet.luxels.core.math

import io.kotest.core.spec.style.DescribeSpec

class Vector4Spec : DescribeSpec({

    include(
        abstractVectorSpec(Dimension.D4),
    )
})
