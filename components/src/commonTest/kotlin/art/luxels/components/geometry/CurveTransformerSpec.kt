package art.luxels.components.geometry

import art.luxels.components.test.kotest.assertions.shouldBeCloseTo
import io.kotest.core.spec.style.DescribeSpec

class CurveTransformerSpec : DescribeSpec(
    {
        describe("a CurveTransformer") {
            it("transform each point into designated result") {
                val curvePoints = listOf(
                    0.0 to 1.0,
                    1.0 to 3.0,
                    2.0 to 2.0,
                )
                val curve = CurveTransformer(curvePoints)

                curvePoints.forEach {
                    val result = curve.transform(it.first)

                    result shouldBeCloseTo it.second
                }
            }
        }
    },
)
