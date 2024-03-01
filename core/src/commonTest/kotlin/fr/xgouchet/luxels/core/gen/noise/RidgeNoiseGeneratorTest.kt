package fr.xgouchet.luxels.core.gen.noise

import io.kotest.core.spec.style.DescribeSpec

class RidgeNoiseGeneratorTest : DescribeSpec({

    include(abstractDelegatedNoiseGeneratorTest("Ridge") { RidgeNoiseGenerator(it) })
})
