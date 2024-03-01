package fr.xgouchet.luxels.core.gen.noise

import io.kotest.core.spec.style.DescribeSpec

class PingPongNoiseGeneratorTest : DescribeSpec({

    include(abstractDelegatedNoiseGeneratorTest("PingPong") { PingPongNoiseGenerator(it, 2.0) })
})
