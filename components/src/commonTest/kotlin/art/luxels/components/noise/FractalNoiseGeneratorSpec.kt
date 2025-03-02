package art.luxels.components.noise

import io.kotest.core.spec.style.DescribeSpec

class FractalNoiseGeneratorSpec : DescribeSpec({

    include(
        abstractDelegatedNoiseGeneratorSpec("Fractal") {
            FractalNoiseGenerator(it, 4)
        },
    )

    include(
        abstractDelegatedNoiseGeneratorSpec("Fractal-White") {
            FractalNoiseGenerator.whiteNoise(it)
        },
    )

    include(
        abstractDelegatedNoiseGeneratorSpec("Fractal-Pink") {
            FractalNoiseGenerator.pinkNoise(it)
        },
    )

    include(
        abstractDelegatedNoiseGeneratorSpec("Fractal-Brown") {
            FractalNoiseGenerator.brownNoise(it)
        },
    )

    include(
        abstractDelegatedNoiseGeneratorSpec("Fractal-Blue") {
            FractalNoiseGenerator.blueNoise(it)
        },
    )

    include(
        abstractDelegatedNoiseGeneratorSpec("Fractal-Violet") {
            FractalNoiseGenerator.violetNoise(it)
        },
    )
})
