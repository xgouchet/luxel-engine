package fr.xgouchet.luxels.engine.render

import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.order
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.render.Exposure
import fr.xgouchet.luxels.engine.test.kotest.property.colorArb
import fr.xgouchet.luxels.engine.test.kotest.property.vectorArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.describeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

fun <D : Dimension> abstractProjectionExposureSpec(dimension: D) = describeSpec {
    describe("expose [$dimension]") {
        it("exposes projected pixels") {
            checkAll(
                vectorArb(dimension),
                Arb.list(vectorArb(Dimension.D2)),
                colorArb(),
                colorArb(),
            ) { input, outputs, inColor, outColor ->
                val projection = mock<Projection<D>> {
                    every { project(input, inColor) } returns outputs.map { vector -> vector to outColor }
                }
                val delegateExposure = mock<Exposure<Dimension.D2>>()
                val testedExposure = ProjectionExposure(delegateExposure, projection)

                testedExposure.expose(input, inColor)

                verify(order) {
                    outputs.forEach { outVector ->
                        delegateExposure.expose(outVector, outColor)
                    }
                }
            }
        }
    }
}

class ProjectionExposureSpec : DescribeSpec(
    {
        include(abstractProjectionExposureSpec(Dimension.D1))
        include(abstractProjectionExposureSpec(Dimension.D2))
        include(abstractProjectionExposureSpec(Dimension.D3))
        include(abstractProjectionExposureSpec(Dimension.D4))
    },
)
