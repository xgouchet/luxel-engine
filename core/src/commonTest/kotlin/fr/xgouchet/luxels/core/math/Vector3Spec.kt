package fr.xgouchet.luxels.core.math

import io.kotest.core.spec.style.DescribeSpec

class Vector3Spec : DescribeSpec({

    include(
        abstractVectorSpec(Dimension.D3),
    )
})
