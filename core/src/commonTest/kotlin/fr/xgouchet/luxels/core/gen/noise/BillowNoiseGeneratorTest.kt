package fr.xgouchet.luxels.core.gen.noise

import io.kotest.core.spec.style.DescribeSpec

class BillowNoiseGeneratorTest : DescribeSpec({

    include(abstractDelegatedNoiseGeneratorTest("Billow") { BillowNoiseGenerator(it) })
})
