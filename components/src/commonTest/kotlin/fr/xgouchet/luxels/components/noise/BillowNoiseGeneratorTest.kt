package fr.xgouchet.luxels.components.noise

import io.kotest.core.spec.style.DescribeSpec

class BillowNoiseGeneratorTest : DescribeSpec({

    include(abstractDelegatedNoiseGeneratorTest("Billow") { BillowNoiseGenerator(it) })
})
