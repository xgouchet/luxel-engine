package fr.xgouchet.luxels.core.gen.noise

import io.kotest.core.spec.style.DescribeSpec

class FractalBrownianNoiseGeneratorTest : DescribeSpec({

    include(AbstractDelegatedNoisGeneratorTest("FractalBrownian") { FractalBrownianNoiseGenerator(it, 4) })
})
