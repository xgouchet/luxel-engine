package fr.xgouchet.luxels.components.noise

import io.kotest.core.spec.style.DescribeSpec

class VoronoiNoiseGeneratorSpec : DescribeSpec({
    include(abstractNoiseGeneratorSpec("Voronoi") { VoronoiNoiseGenerator() })
})
