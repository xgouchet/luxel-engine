package fr.xgouchet.luxels.core.gen.noise

import io.kotest.core.spec.style.DescribeSpec

class PerlinNoiseGeneratorTest : DescribeSpec({
    include(abstractNoiseGeneratorTest("Perlin") { PerlinNoiseGenerator() })
})
