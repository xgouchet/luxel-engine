package fr.xgouchet.luxels.components.noise

import io.kotest.core.spec.style.DescribeSpec

class RidgeNoiseGeneratorTest : DescribeSpec({

    include(abstractDelegatedNoiseGeneratorTest("Ridge") { RidgeNoiseGenerator(it) })
})
