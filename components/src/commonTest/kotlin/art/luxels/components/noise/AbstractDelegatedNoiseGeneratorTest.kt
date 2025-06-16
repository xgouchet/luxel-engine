package art.luxels.components.noise

import io.kotest.core.spec.style.describeSpec

fun abstractDelegatedNoiseGeneratorSpec(
    description: String,
    noiseProvider: (DimensionalNoiseGenerator) -> DimensionalNoiseGenerator,
) = describeSpec {
    // Discarded for speed for now
//    include(
//        abstractNoiseGeneratorSpec("$description+Voronoi") {
//            noiseProvider(VoronoiNoiseGenerator())
//        },
//    )

    include(
        abstractNoiseGeneratorSpec("$description+Perlin") {
            noiseProvider(PerlinNoiseGenerator())
        },
    )
}
