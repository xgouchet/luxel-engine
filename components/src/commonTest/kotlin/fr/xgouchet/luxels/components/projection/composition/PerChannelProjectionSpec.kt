package fr.xgouchet.luxels.components.projection.composition

import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.test.kotest.property.colorArb
import fr.xgouchet.luxels.components.test.kotest.property.vectorArb
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.engine.render.Projection
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class PerChannelProjectionSpec : DescribeSpec(
    {
        describe("project") {
            it("combines the channels from three delegates [D3]") {
                checkAll(
                    vectorArb(D3),
                    colorArb(),
                    vectorArb(D2),
                    vectorArb(D2),
                    vectorArb(D2),
                ) { inPos, inCol, redPos, greenPos, bluePos ->

                    val redProj = mock<Projection<D3>> {
                        every { project(inPos, inCol) } returns listOf(redPos to inCol)
                    }
                    val greenProj = mock<Projection<D3>> {
                        every { project(inPos, inCol) } returns listOf(greenPos to inCol)
                    }
                    val blueProj = mock<Projection<D3>> {
                        every { project(inPos, inCol) } returns listOf(bluePos to inCol)
                    }

                    val perChannelProj = PerChannelProjection(redProj, greenProj, blueProj)
                    val result = perChannelProj.project(inPos, inCol)

                    result[0].first shouldBe redPos
                    result[0].second shouldBe inCol * HDRColor.RED
                    result[1].first shouldBe greenPos
                    result[1].second shouldBe inCol * HDRColor.GREEN
                    result[2].first shouldBe bluePos
                    result[2].second shouldBe inCol * HDRColor.BLUE
                }
            }
        }
    },
)
