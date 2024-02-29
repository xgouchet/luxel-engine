package fr.xgouchet.luxels.core.gen.noise

import io.kotest.core.spec.style.describeSpec

fun AbstractDelegatedNoisGeneratorTest(
    description: String,
    noiseProvider: (DimensionalNoiseGenerator) -> DimensionalNoiseGenerator,
) = describeSpec {
    include(
        AbstractNoiseGeneratorTest("$description+Voronoi") {
            noiseProvider(VoronoiNoiseGenerator())
        },
    )

    include(
        AbstractNoiseGeneratorTest("$description+Perlin") {
            noiseProvider(PerlinNoiseGenerator())
        },
    )
}
