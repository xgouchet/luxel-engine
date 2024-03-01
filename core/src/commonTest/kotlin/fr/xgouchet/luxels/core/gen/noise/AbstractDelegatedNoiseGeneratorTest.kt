package fr.xgouchet.luxels.core.gen.noise

import io.kotest.core.spec.style.describeSpec

fun abstractDelegatedNoiseGeneratorTest(
    description: String,
    noiseProvider: (DimensionalNoiseGenerator) -> DimensionalNoiseGenerator,
) = describeSpec {
    include(
        abstractNoiseGeneratorTest("$description+Voronoi") {
            noiseProvider(VoronoiNoiseGenerator())
        },
    )

    include(
        abstractNoiseGeneratorTest("$description+Perlin") {
            noiseProvider(PerlinNoiseGenerator())
        },
    )
}
