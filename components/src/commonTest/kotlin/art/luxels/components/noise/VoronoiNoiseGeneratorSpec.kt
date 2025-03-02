package art.luxels.components.noise

import io.kotest.core.spec.style.DescribeSpec

class VoronoiNoiseGeneratorSpec : DescribeSpec({
    include(abstractNoiseGeneratorSpec("Voronoi") { VoronoiNoiseGenerator() })
})
