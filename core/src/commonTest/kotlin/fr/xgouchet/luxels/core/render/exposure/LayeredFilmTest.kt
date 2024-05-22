package fr.xgouchet.luxels.core.render.exposure

import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.math.geometry.Vector2
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.property.colorArb
import fr.xgouchet.luxels.core.test.kotest.property.resolutionArb
import fr.xgouchet.luxels.core.test.kotest.property.vector2Arb
import fr.xgouchet.luxels.core.test.stub.StubFilm
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldNotBeEqual
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.assume
import io.kotest.property.checkAll

class LayeredFilmTest : DescribeSpec({

    include(abstractFilmTest(false) { resolution -> LayeredFilm(resolution) })

    describe("expose") {
        it("should throw") {
            checkAll(vector2Arb(), colorArb(), resolutionArb()) { v, color, resolution ->
                val layeredFilm = LayeredFilm(resolution)
                shouldThrowAny {
                    layeredFilm.expose(v, color)
                }
            }
        }
    }

    describe("mergeLayer") {

        it("adds layer color to existing one") {
            checkAll(
                Arb.double(min = 1.0, max = 319.0),
                Arb.double(min = 1.0, max = 239.0),
                colorArb(),
                colorArb(),
            ) { x, y, c, o ->
                val layeredFilm = LayeredFilm(Resolution.QVGA)
                val stubFilm = StubFilm(Resolution.QVGA)

                val p = Vector2(x, y).floor()
                val i = p.x.toInt()
                val j = p.y.toInt()

                stubFilm.stubColor(i, j, o)
                layeredFilm.expose(i, j, c, 1.0)
                layeredFilm.mergeLayer(stubFilm)

                val color = layeredFilm.getColor(i, j)
                color shouldBeCloseTo (c + o)
            }
        }

        it("throws an error if merging a film with a different size") {
            checkAll(
                resolutionArb(),
                resolutionArb(),
            ) { resolution, otherResolution ->
                assume {
                    resolution shouldNotBeEqual otherResolution
                }

                val layeredFilm = LayeredFilm(resolution)
                val stubFilm = StubFilm(otherResolution)

                shouldThrowAny {
                    layeredFilm.mergeLayer(stubFilm)
                }
            }
        }
    }
})
