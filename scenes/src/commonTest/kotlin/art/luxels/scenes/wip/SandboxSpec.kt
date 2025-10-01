package art.luxels.scenes.wip

import art.luxels.components.color.WLColorSource
import art.luxels.core.math.Vector2
import art.luxels.scenes.wip.rain.RainLuxel
import io.kotest.core.spec.style.DescribeSpec

class SandboxSpec : DescribeSpec(
    {

        it("computes a reductionFactor for blue") {
            val luxel = RainLuxel(
                32,
                WLColorSource.MEAN_BLUE,
                Vector2(0.0, 0.0),
                Vector2(0.0, 0.0),
                0.0,
            )

            println("BLUE -> ${luxel.reductionFactor}")
            println("BLUE -> ${luxel.colorSource.color()}")
        }

        it("computes a reductionFactor for green") {
            val luxel = RainLuxel(
                32,
                WLColorSource.MEAN_GREEN,
                Vector2(0.0, 0.0),
                Vector2(0.0, 0.0),
                0.0,
            )

            println("GREEN -> ${luxel.reductionFactor}")
            println("GREEN -> ${luxel.colorSource.color()}")
        }

        it("computes a reductionFactor for red") {
            val luxel = RainLuxel(
                32,
                WLColorSource.MEAN_RED,
                Vector2(0.0, 0.0),
                Vector2(0.0, 0.0),
                0.0,
            )

            println("RED -> ${luxel.reductionFactor}")
            println("RED -> ${luxel.colorSource.color()}")
        }
    },
)
