package art.luxels.components.noise

import io.kotest.core.spec.style.DescribeSpec

class PingPongNoiseGeneratorSpec : DescribeSpec({

    include(abstractDelegatedNoiseGeneratorSpec("PingPong") { PingPongNoiseGenerator(it, 2.0) })
})
