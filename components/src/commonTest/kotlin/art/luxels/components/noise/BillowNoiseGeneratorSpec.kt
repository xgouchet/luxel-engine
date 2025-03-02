package art.luxels.components.noise

import io.kotest.core.spec.style.DescribeSpec

class BillowNoiseGeneratorSpec : DescribeSpec({

    include(abstractDelegatedNoiseGeneratorSpec("Billow") { BillowNoiseGenerator(it) })
})
