package fr.xgouchet.luxels.components.noise

import io.kotest.core.spec.style.DescribeSpec

class VoronoiNoiseGeneratorTest : DescribeSpec({
    include(abstractNoiseGeneratorTest("Voronoi") { VoronoiNoiseGenerator() })
})
