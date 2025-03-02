package art.luxels.components.noise

import io.kotest.core.spec.style.DescribeSpec

class PerlinNoiseGeneratorSpec : DescribeSpec({
    include(abstractNoiseGeneratorSpec("Perlin") { PerlinNoiseGenerator() })
})
