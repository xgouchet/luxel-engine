package fr.xgouchet.luxels.core.render.exposure

import dev.mokkery.answering.calls
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.not
import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.x
import fr.xgouchet.luxels.core.math.y
import fr.xgouchet.luxels.core.render.Film
import fr.xgouchet.luxels.core.render.LayeredFilm
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.property.colorArb
import fr.xgouchet.luxels.core.test.kotest.property.resolutionArb
import fr.xgouchet.luxels.core.test.kotest.property.vectorArb
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldNotBeEqual
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.enum
import io.kotest.property.assume
import io.kotest.property.checkAll

class LayeredFilmSpec : DescribeSpec(
    {

        val vector2Arb = { vectorArb(Dimension.D2) }
        include(abstractFilmSpec(false) { resolution -> LayeredFilm(resolution) })

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
            it("doesn't read the layer if it has no data") {

                checkAll(
                    Arb.double(min = 1.0, max = 127.0),
                    Arb.double(min = 1.0, max = 127.0),
                    colorArb(),
                    colorArb(),
                ) { x, y, c, o ->
                    val p = Vector2(x, y).floor()
                    val i = p.x.toInt()
                    val j = p.y.toInt()
                    val layeredFilm = LayeredFilm(Resolution.SQUARE_128)
                    val layer = mock<Film> {
                        every { width } returns 128
                        every { height } returns 128
                        every { hasData() } returns false
                    }

                    layeredFilm.expose(i, j, c, 1.0)
                    layeredFilm.mergeLayer(layer)

                    verify(not) { layer.getColor(any(), any()) }
                }
            }

            it("adds layer color to existing one") {
                checkAll(
                    Arb.double(min = 1.0, max = 127.0),
                    Arb.double(min = 1.0, max = 127.0),
                    colorArb(),
                    colorArb(),
                ) { x, y, c, o ->
                    val p = Vector2(x, y).floor()
                    val i = p.x.toInt()
                    val j = p.y.toInt()
                    val layeredFilm = LayeredFilm(Resolution.SQUARE_128)
                    val layer = mock<Film> {
                        every { width } returns 128
                        every { height } returns 128
                        every { hasData() } returns true
                        every { getColor(any(), any()) } calls { (argI: Int, argJ: Int) ->
                            if (argI == i && argJ == j) o else HDRColor.TRANSPARENT
                        }
                    }

                    layeredFilm.expose(i, j, c, 1.0)
                    layeredFilm.mergeLayer(layer)

                    val color = layeredFilm.getColor(i, j)
                    color shouldBeCloseTo (c + o)
                }
            }

            it("throws an error if merging a film with a different size") {
                checkAll(
                    Arb.enum<Resolution>(),
                    Arb.enum<Resolution>(),
                ) { resolution, otherResolution ->
                    assume {
                        resolution shouldNotBeEqual otherResolution
                    }

                    val layeredFilm = LayeredFilm(resolution)
                    val layer = mock<Film> {
                        every { width } returns otherResolution.width
                        every { height } returns otherResolution.height
                    }

                    shouldThrowAny {
                        layeredFilm.mergeLayer(layer)
                    }
                }
            }
        }
    },
)
