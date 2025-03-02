package art.luxels.components.noise

import io.kotest.core.spec.style.DescribeSpec

class RidgeNoiseGeneratorSpec : DescribeSpec({

    include(abstractDelegatedNoiseGeneratorSpec("Ridge") { RidgeNoiseGenerator(it) })
})
