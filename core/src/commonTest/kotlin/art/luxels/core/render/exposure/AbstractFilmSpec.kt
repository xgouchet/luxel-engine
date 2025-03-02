@file:OptIn(KotestInternal::class)

package art.luxels.core.render.exposure

import art.luxels.core.math.Vector2
import art.luxels.core.render.AbstractFilm
import art.luxels.core.render.Resolution
import art.luxels.core.test.kotest.assertions.shouldBeCloseTo
import art.luxels.core.test.kotest.property.colorArb
import art.luxels.core.test.kotest.property.resolutionArb
import art.luxels.imageio.color.HDRColor
import io.kotest.common.ExperimentalKotest
import io.kotest.common.KotestInternal
import io.kotest.core.spec.style.describeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll
import kotlin.math.ceil
import kotlin.math.floor

@OptIn(ExperimentalKotest::class)
internal fun abstractFilmSpec(supportsDirectExposure: Boolean = true, filmProvider: (Resolution) -> AbstractFilm) =
    describeSpec {
        describe("exposeLuxel (int)") {
            it("does nothing when exposing outside the bounds of the film (-x)") {
                checkAll(
                    resolutionArb(),
                    Arb.int(max = -1),
                    Arb.int(),
                    colorArb(),
                    Arb.double(0.1, 100.0),
                ) { resolution, i, j, color, intensity ->
                    val film = filmProvider(resolution)

                    film.expose(i, j, color, intensity)

                    film.getColor(i, j) shouldBeCloseTo HDRColor.TRANSPARENT
                }
            }

            it("does nothing when exposing outside the bounds of the film (-y)") {
                checkAll(
                    resolutionArb(),
                    Arb.int(),
                    Arb.int(max = -1),
                    colorArb(),
                    Arb.double(0.1, 100.0),
                ) { resolution, i, j, color, intensity ->
                    val film = filmProvider(resolution)

                    film.expose(i, j, color, intensity)

                    film.getColor(i, j) shouldBeCloseTo HDRColor.TRANSPARENT
                }
            }

            it("does nothing when exposing outside the bounds of the film (x++)") {
                checkAll(
                    resolutionArb(),
                    Arb.int(min = 8192),
                    Arb.int(),
                    colorArb(),
                    Arb.double(0.1, 100.0),
                ) { resolution, i, j, color, intensity ->
                    val film = filmProvider(resolution)

                    film.expose(i, j, color, intensity)

                    film.getColor(i, j) shouldBeCloseTo HDRColor.TRANSPARENT
                }
            }

            it("does nothing when exposing outside the bounds of the film (y++)") {
                checkAll(
                    resolutionArb(),
                    Arb.int(),
                    Arb.int(min = 8192),
                    colorArb(),
                    Arb.double(0.1, 100.0),
                ) { resolution, i, j, color, intensity ->
                    val film = filmProvider(resolution)

                    film.expose(i, j, color, intensity)

                    film.getColor(i, j) shouldBeCloseTo HDRColor.TRANSPARENT
                }
            }

            it("exposes a pixel with the given color") {
                checkAll(
                    resolutionArb(),
                    Arb.positiveInt(max = 65536),
                    Arb.positiveInt(max = 65536),
                    colorArb(),
                ) { resolution, i, j, color ->
                    val film = filmProvider(resolution)

                    film.expose(i % resolution.width, j % resolution.height, color, 1.0)

                    film.getColor(i % resolution.width, j % resolution.height) shouldBeCloseTo color
                }
            }

            it("exposes a pixel with the given intensity") {
                checkAll(
                    resolutionArb(),
                    Arb.positiveInt(max = 65536),
                    Arb.positiveInt(max = 65536),
                    Arb.double(0.1, 100.0),
                ) { resolution, i, j, intensity ->
                    val film = filmProvider(resolution)
                    val expectedColor = HDRColor(intensity, intensity, intensity, intensity)

                    film.expose(i % resolution.width, j % resolution.height, HDRColor.WHITE, intensity)

                    film.getColor(i % resolution.width, j % resolution.height) shouldBeCloseTo expectedColor
                }
            }
        }

        describe("exposeLuxel (double)").config(enabled = supportsDirectExposure) {
            it("does nothing when exposing outside the bounds of the film (-x)") {
                checkAll(
                    resolutionArb(),
                    Arb.double(max = -2.0),
                    Arb.double(),
                    colorArb(),
                ) { resolution, x, y, color ->
                    val film = filmProvider(resolution)

                    film.expose(Vector2(x, y), color)

                    for (i in floor(x).toInt()..ceil(x).toInt()) {
                        for (j in floor(y).toInt()..ceil(y).toInt()) {
                            film.getColor(i, j) shouldBeCloseTo HDRColor.TRANSPARENT
                        }
                    }
                }
            }
            it("does nothing when exposing outside the bounds of the film (-y)") {
                checkAll(
                    resolutionArb(),
                    Arb.double(),
                    Arb.double(max = -2.0),
                    colorArb(),
                ) { resolution, x, y, color ->
                    val film = filmProvider(resolution)

                    film.expose(Vector2(x, y), color)

                    for (i in floor(x).toInt()..ceil(x).toInt()) {
                        for (j in floor(y).toInt()..ceil(y).toInt()) {
                            film.getColor(i, j) shouldBeCloseTo HDRColor.TRANSPARENT
                        }
                    }
                }
            }

            it("does nothing when exposing outside the bounds of the film (x++)") {
                checkAll(
                    resolutionArb(),
                    Arb.double(min = 8192.0),
                    Arb.double(),
                    colorArb(),
                ) { resolution, x, y, color ->
                    val film = filmProvider(resolution)

                    film.expose(Vector2(x, y), color)

                    for (i in floor(x).toInt()..ceil(x).toInt()) {
                        for (j in floor(y).toInt()..ceil(y).toInt()) {
                            film.getColor(i, j) shouldBeCloseTo HDRColor.TRANSPARENT
                        }
                    }
                }
            }

            it("does nothing when exposing outside the bounds of the film (y++)") {
                checkAll(
                    resolutionArb(),
                    Arb.double(),
                    Arb.double(min = 8192.0),
                    colorArb(),
                ) { resolution, x, y, color ->
                    val film = filmProvider(resolution)

                    film.expose(Vector2(x, y), color)

                    for (i in floor(x).toInt()..ceil(x).toInt()) {
                        for (j in floor(y).toInt()..ceil(y).toInt()) {
                            film.getColor(i, j) shouldBeCloseTo HDRColor.TRANSPARENT
                        }
                    }
                }
            }
        }

        describe("hasData") {
            it("returns false by default") {
                checkAll(
                    resolutionArb(),
                ) { resolution ->
                    val film = filmProvider(resolution)

                    val hasData = film.hasData()

                    hasData shouldBe false
                }
            }

            it("returns true after exposure") {
                checkAll(
                    resolutionArb(),
                    Arb.int(),
                    Arb.int(max = -1),
                    colorArb(),
                    Arb.double(0.1, 100.0),
                ) { resolution, i, j, color, intensity ->
                    val film = filmProvider(resolution)

                    film.expose(i, j, color, intensity)
                    val hasData = film.hasData()

                    hasData shouldBe true
                }
            }
        }
    }
