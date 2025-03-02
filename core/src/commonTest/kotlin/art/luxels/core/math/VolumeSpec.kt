package art.luxels.core.math

import io.kotest.core.spec.style.DescribeSpec

class VolumeSpec : DescribeSpec({

    include(abstractVolumeSpec(Dimension.D1))
    include(abstractVolumeSpec(Dimension.D2))
    include(abstractVolumeSpec(Dimension.D3))
    include(abstractVolumeSpec(Dimension.D4))
})
