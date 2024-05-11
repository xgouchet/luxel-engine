package fr.xgouchet.luxels.core.math

import io.kotest.core.spec.style.DescribeSpec

class Vector2Spec : DescribeSpec({

    include(
        abstractVectorSpec(Dimension.D2),
    )
})
