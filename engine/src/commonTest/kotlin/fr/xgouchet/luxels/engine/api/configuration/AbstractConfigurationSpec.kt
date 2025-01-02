package fr.xgouchet.luxels.engine.api.configuration

import fr.xgouchet.luxels.core.io.ImageFixer
import fr.xgouchet.luxels.core.io.NoOpFixer
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.w
import fr.xgouchet.luxels.core.math.x
import fr.xgouchet.luxels.core.math.y
import fr.xgouchet.luxels.core.math.z
import fr.xgouchet.luxels.core.render.Film
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.input.InputData
import fr.xgouchet.luxels.engine.test.kotest.property.durationArb
import fr.xgouchet.luxels.engine.test.kotest.property.volumeArb
import io.kotest.core.spec.style.describeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.enum
import kotlin.time.Duration.Companion.seconds

@Suppress("LocalVariableName", "NonAsciiCharacters", "ktlint:standard:property-naming")
fun <D : Dimension, I : Any> abstractConfigurationSpec(
    name: String,
    dimension: D,
    expectedInput: List<InputData<I>>?,
    instantiation: (ConfigurationBuilder<D, I>.() -> Unit) -> Configuration<D, I>,
) = describeSpec {
    describe("$name [$dimension]") {
        describe("input") {
            it("has expected inputs") {
                if (expectedInput != null) {
                    val configuration = instantiation {}
                    val inputData = configuration.input.source.toList()

                    inputData shouldBe expectedInput
                }
            }
        }

        describe("simulation") {
            it("has sensible defaults") {
                val configuration = instantiation {}

                configuration.simulation.quality shouldBe Quality.GRAINY_1
                configuration.simulation.volume shouldBe Volume.unit(dimension)
                configuration.simulation.maxThreadCount shouldBe 4
                configuration.simulation.simulationType shouldBe SimulationType.RENDER
            }

            it("sets the pass type") {
                checkAll(Exhaustive.enum<SimulationType>()) { p ->
                    val configuration = instantiation {
                        simulation {
                            passType(p)
                        }
                    }

                    configuration.simulation.simulationType shouldBe p
                }
            }

            it("sets the quality") {
                checkAll(Exhaustive.enum<Quality>()) { q ->
                    val configuration = instantiation {
                        simulation {
                            quality(q)
                        }
                    }

                    configuration.simulation.quality shouldBe q
                }
            }

            it("sets the space") {
                checkAll(volumeArb(dimension)) { volume ->
                    val configuration = instantiation {
                        simulation {
                            space(volume)
                        }
                    }

                    configuration.simulation.volume shouldBe volume
                }
            }

            it("sets the maximumThreadCount") {
                checkAll(Arb.int(1..128)) { count ->
                    val configuration = instantiation {
                        simulation {
                            maximumThreadCount(count)
                        }
                    }

                    configuration.simulation.maxThreadCount shouldBe count
                }
            }
        }

        describe("render") {
            it("has sensible defaults") {
                val configuration = instantiation {}

                configuration.render.filmType shouldBe FilmType.ROUGH
                (configuration.render.fixer is NoOpFixer) shouldBe true
                configuration.render.resolution shouldBe Resolution.XGA
            }

            it("sets the film type to clean") {
                val configuration = instantiation {
                    render {
                        filmType(FilmType.CLEAN)
                    }
                }

                configuration.render.filmType shouldBe FilmType.CLEAN
            }

            it("sets the film type to rough") {
                val configuration = instantiation {
                    render {
                        filmType(FilmType.ROUGH)
                    }
                }
                configuration.render.filmType shouldBe FilmType.ROUGH
            }

            it("sets the resolution") {
                checkAll(Exhaustive.enum<Resolution>()) { resolution ->
                    val configuration = instantiation {
                        render {
                            resolution(resolution)
                        }
                    }

                    configuration.render.resolution shouldBe resolution
                }
            }

            it("sets the fixer") {
                val fixer = object : ImageFixer {
                    override fun write(film: Film, outputName: String) {
                        TODO("Not yet implemented")
                    }
                }
                val configuration = instantiation {
                    render {
                        fixer(fixer)
                    }
                }

                configuration.render.fixer shouldBe fixer
            }
        }

        describe("animation") {
            it("has sensible defaults") {
                val configuration = instantiation {}

                configuration.animation.duration shouldBe 0.seconds
                configuration.animation.fps shouldBe 24
            }

            it("sets the duration") {
                checkAll(durationArb()) { duration ->
                    val configuration = instantiation {
                        animation {
                            duration(duration)
                        }
                    }

                    configuration.animation.duration shouldBe duration
                }
            }

            it("sets the fps") {
                checkAll(Arb.int(1, 1024)) { fps ->
                    val configuration = instantiation {
                        animation {
                            fps(fps)
                            duration(10.seconds)
                        }
                    }

                    configuration.animation.fps shouldBe fps
                }
            }
        }

        describe("density") {
            it("scales simulation space with the resolution") {
                checkAll(Exhaustive.enum<Resolution>(), Arb.double(0.01, 100.0)) { resolution, density ->
                    val configuration = instantiation {
                        render {
                            resolution(resolution)
                        }
                        simulationVolumeDensity(density)
                    }

                    configuration.render.resolution shouldBe resolution
                    val volume = configuration.simulation.volume
                    volume.min shouldBe Vector.nul(dimension)
                    volume.size.x shouldBe resolution.width * density
                    if (dimension.size >= 2) {
                        volume.size.y shouldBe resolution.height * density
                    }
                    if (dimension.size >= 3) {
                        volume.size.z shouldBe resolution.width * density
                    }
                    if (dimension.size >= 4) {
                        volume.size.w shouldBe resolution.height * density
                    }
                }
            }
        }
    }
}
