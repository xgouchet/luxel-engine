package fr.xgouchet.luxels.components.noise

import io.kotest.core.spec.style.DescribeSpec

class PerlinNoiseGeneratorTest : DescribeSpec({
    include(abstractNoiseGeneratorTest("Perlin") { PerlinNoiseGenerator() })
})
