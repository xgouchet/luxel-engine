package fr.xgouchet.luxels.core.gen.noise

import io.kotest.core.spec.style.DescribeSpec

class VoronoiNoiseGeneratorTest : DescribeSpec({
    include(abstractNoiseGeneratorTest("Voronoi") { VoronoiNoiseGenerator() })
})