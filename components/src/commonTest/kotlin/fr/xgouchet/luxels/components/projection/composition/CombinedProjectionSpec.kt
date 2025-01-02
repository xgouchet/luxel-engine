package fr.xgouchet.luxels.components.projection.composition

import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import fr.xgouchet.luxels.components.test.kotest.property.colorArb
import fr.xgouchet.luxels.components.test.kotest.property.vectorArb
import fr.xgouchet.luxels.components.test.kotest.property.vectorListArb
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.engine.render.Projection
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class CombinedProjectionSpec : DescribeSpec(
    {
        describe("project") {
            it("combines the channels from all delegates [D3]") {
                checkAll(
                    vectorArb(D3),
                    colorArb(),
                    vectorListArb(D2),
                ) { inPos, inCol, delegatePos ->

                    val delegates = delegatePos.map { pos ->
                        mock<Projection<D3>> {
                            every { project(inPos, inCol) } returns listOf(pos to inCol)
                        }
                    }

                    val combinedProj = CombinedProjection(*delegates.toTypedArray())
                    val result = combinedProj.project(inPos, inCol)

                    result.forEachIndexed { index, pair ->
                        pair.first shouldBe delegatePos[index]
                        pair.second shouldBe inCol
                    }
                }
            }
        }
    },
)
