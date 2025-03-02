package art.luxels.engine.render

import art.luxels.core.render.CleanFilm
import art.luxels.core.render.Resolution
import art.luxels.core.render.RoughFilm
import art.luxels.engine.api.configuration.FilmType
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.enum
import io.kotest.property.checkAll

class DefaultFilmProviderSpec : DescribeSpec(
    {
        describe("createFilm") {
            it("creates a ROUGH film") {
                checkAll(Arb.Companion.enum<Resolution>()) { resolution ->
                    val film = DefaultFilmProvider().createFilm(FilmType.ROUGH, resolution)

                    film.shouldBeTypeOf<RoughFilm>()
                    film.resolution shouldBe resolution
                }
            }

            it("creates a CLEAN film") {
                checkAll(Arb.Companion.enum<Resolution>()) { resolution ->
                    val film = DefaultFilmProvider().createFilm(FilmType.CLEAN, resolution)

                    film.shouldBeTypeOf<CleanFilm>()
                    film.resolution shouldBe resolution
                }
            }
        }
    },
)
