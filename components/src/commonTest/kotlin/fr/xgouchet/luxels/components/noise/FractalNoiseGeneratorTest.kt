package fr.xgouchet.luxels.components.noise

import io.kotest.core.spec.style.DescribeSpec

class FractalNoiseGeneratorTest : DescribeSpec({

    include(
        abstractDelegatedNoiseGeneratorTest("Fractal") {
            FractalNoiseGenerator(it, 4)
        },
    )

    include(
        abstractDelegatedNoiseGeneratorTest("Fractal-White") {
            FractalNoiseGenerator.whiteNoise(it)
        },
    )

    include(
        abstractDelegatedNoiseGeneratorTest("Fractal-Pink") {
            FractalNoiseGenerator.pinkNoise(it)
        },
    )

    include(
        abstractDelegatedNoiseGeneratorTest("Fractal-Brown") {
            FractalNoiseGenerator.brownNoise(it)
        },
    )

    include(
        abstractDelegatedNoiseGeneratorTest("Fractal-Blue") {
            FractalNoiseGenerator.blueNoise(it)
        },
    )

    include(
        abstractDelegatedNoiseGeneratorTest("Fractal-Violet") {
            FractalNoiseGenerator.violetNoise(it)
        },
    )
})
