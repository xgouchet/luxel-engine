package fr.xgouchet.luxels.core.configuration

import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.io.ImageFixer
import fr.xgouchet.luxels.core.io.NoOpFixer
import fr.xgouchet.luxels.core.log.Logger
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.w
import fr.xgouchet.luxels.core.math.x
import fr.xgouchet.luxels.core.math.y
import fr.xgouchet.luxels.core.math.z
import fr.xgouchet.luxels.core.render.FrameInfo
import fr.xgouchet.luxels.core.render.exposure.CleanFilm
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.render.exposure.RoughFilm
import fr.xgouchet.luxels.core.simulation.worker.AbstractSimulationWorker
import fr.xgouchet.luxels.core.simulation.worker.DeathSimulationWorker
import fr.xgouchet.luxels.core.simulation.worker.EnvSimulationWorker
import fr.xgouchet.luxels.core.simulation.worker.PathSimulationWorker
import fr.xgouchet.luxels.core.simulation.worker.RenderSimulationWorker
import fr.xgouchet.luxels.core.simulation.worker.SpawnSimulationWorker
import fr.xgouchet.luxels.core.test.kotest.property.durationArb
import fr.xgouchet.luxels.core.test.kotest.property.volumeArb
import fr.xgouchet.luxels.core.test.stub.StubFilm
import fr.xgouchet.luxels.core.test.stub.StubLogHandler
import fr.xgouchet.luxels.core.test.stub.StubProjection
import fr.xgouchet.luxels.core.test.stub.StubSimulator
import io.kotest.core.spec.style.describeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.enum
import kotlin.math.max
import kotlin.time.Duration.Companion.seconds

@Suppress("LocalVariableName", "NonAsciiCharacters", "ktlint:standard:property-naming")
fun <D : Dimension, I : Any> abstractConfigurationSpec(
    name: String,
    dimension: D,
    expectedInput: List<InputData<I>>,
    instantiation: (ConfigurationBuilder<D, I>.() -> Unit) -> Configuration<D, I>,
) = describeSpec {
    describe("$name [$dimension]") {
        describe("input") {
            it("has expected inputs") {
                val configuration = instantiation {}
                val inputData = configuration.input.source.toList()

                inputData shouldBe expectedInput
            }
        }

        describe("simulation") {
            it("has sensible defaults") {
                val configuration = instantiation {}

                configuration.simulation.quality shouldBe Quality.DEBUG
                configuration.simulation.volume shouldBe Volume.unit(dimension)
                configuration.simulation.maxThreadCount shouldBe 4
                configuration.simulation.passType shouldBe PassType.RENDER
            }

            it("sets the pass type") {
                checkAll(Exhaustive.enum<PassType>()) { p ->
                    val configuration = instantiation {
                        simulation {
                            passType(p)
                        }
                    }

                    configuration.simulation.passType shouldBe p
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

            it("creates a Render worker") {
                checkAll(
                    Exhaustive.enum<Resolution>(),
                    Arb.int(0, 1024),
                    durationArb(),
                    Arb.long(1024L, 65536L),
                ) { resolution, frameIndex, duration, luxelCount ->

                    val configuration = instantiation {
                        simulation { passType(PassType.RENDER) }
                        render { resolution(resolution) }
                    }

                    val stubSimulator = StubSimulator<D, I>()
                    val stubFilm = StubFilm(resolution)
                    val stubProjection = StubProjection<D>()
                    val logger = Logger(StubLogHandler())
                    val frameInfo = FrameInfo(frameIndex, duration)
                    val worker = configuration.createWorker(
                        stubSimulator,
                        stubFilm,
                        frameInfo,
                        luxelCount,
                        stubProjection,
                        logger,
                    ) as AbstractSimulationWorker<*, *, *>

                    (worker is RenderSimulationWorker<*, *, *>) shouldBe true
                    worker.film shouldBeSameInstanceAs stubFilm
                    worker.simulator shouldBeSameInstanceAs stubSimulator
                    worker.simulation shouldBeSameInstanceAs configuration.simulation
                    worker.projection shouldBeSameInstanceAs stubProjection
                    worker.logger shouldBeSameInstanceAs logger
                    worker.time shouldBe duration
                    worker.luxelCountPerThread shouldBe luxelCount
                }
            }

            it("creates a Env worker") {
                checkAll(
                    Exhaustive.enum<Resolution>(),
                    Arb.int(0, 1024),
                    durationArb(),
                    Arb.long(1024L, 65536L),
                ) { resolution, frameIndex, duration, luxelCount ->

                    val configuration = instantiation {
                        simulation { passType(PassType.ENV) }
                        render { resolution(resolution) }
                    }

                    val stubSimulator = StubSimulator<D, I>()
                    val stubFilm = StubFilm(resolution)
                    val stubProjection = StubProjection<D>()
                    val logger = Logger(StubLogHandler())
                    val frameInfo = FrameInfo(frameIndex, duration)
                    val worker = configuration.createWorker(
                        stubSimulator,
                        stubFilm,
                        frameInfo,
                        luxelCount,
                        stubProjection,
                        logger,
                    ) as AbstractSimulationWorker<*, *, *>

                    (worker is EnvSimulationWorker<*, *, *>) shouldBe true
                    worker.film shouldBeSameInstanceAs stubFilm
                    worker.simulator shouldBeSameInstanceAs stubSimulator
                    worker.simulation shouldBeSameInstanceAs configuration.simulation
                    worker.projection shouldBeSameInstanceAs stubProjection
                    worker.logger shouldBeSameInstanceAs logger
                    worker.time shouldBe duration
                    worker.luxelCountPerThread shouldBe luxelCount
                }
            }

            it("creates a Death worker") {
                checkAll(
                    Exhaustive.enum<Resolution>(),
                    Arb.int(0, 1024),
                    durationArb(),
                    Arb.long(1024L, 65536L),
                ) { resolution, frameIndex, duration, luxelCount ->

                    val configuration = instantiation {
                        simulation { passType(PassType.DEATH) }
                        render { resolution(resolution) }
                    }

                    val stubSimulator = StubSimulator<D, I>()
                    val stubFilm = StubFilm(resolution)
                    val stubProjection = StubProjection<D>()
                    val logger = Logger(StubLogHandler())
                    val frameInfo = FrameInfo(frameIndex, duration)
                    val worker = configuration.createWorker(
                        stubSimulator,
                        stubFilm,
                        frameInfo,
                        luxelCount,
                        stubProjection,
                        logger,
                    ) as AbstractSimulationWorker<*, *, *>

                    (worker is DeathSimulationWorker<*, *, *>) shouldBe true
                    worker.film shouldBeSameInstanceAs stubFilm
                    worker.simulator shouldBeSameInstanceAs stubSimulator
                    worker.simulation shouldBeSameInstanceAs configuration.simulation
                    worker.projection shouldBeSameInstanceAs stubProjection
                    worker.logger shouldBeSameInstanceAs logger
                    worker.time shouldBe duration
                    worker.luxelCountPerThread shouldBe luxelCount
                }
            }

            it("creates a Path worker") {
                checkAll(
                    Exhaustive.enum<Resolution>(),
                    Arb.int(0, 1024),
                    durationArb(),
                    Arb.long(1024L, 65536L),
                ) { resolution, frameIndex, duration, luxelCount ->

                    val configuration = instantiation {
                        simulation { passType(PassType.PATH) }
                        render { resolution(resolution) }
                    }

                    val stubSimulator = StubSimulator<D, I>()
                    val stubFilm = StubFilm(resolution)
                    val stubProjection = StubProjection<D>()
                    val logger = Logger(StubLogHandler())
                    val frameInfo = FrameInfo(frameIndex, duration)
                    val worker = configuration.createWorker(
                        stubSimulator,
                        stubFilm,
                        frameInfo,
                        luxelCount,
                        stubProjection,
                        logger,
                    ) as AbstractSimulationWorker<*, *, *>

                    (worker is PathSimulationWorker<*, *, *>) shouldBe true
                    worker.film shouldBeSameInstanceAs stubFilm
                    worker.simulator shouldBeSameInstanceAs stubSimulator
                    worker.simulation shouldBeSameInstanceAs configuration.simulation
                    worker.projection shouldBeSameInstanceAs stubProjection
                    worker.logger shouldBeSameInstanceAs logger
                    worker.time shouldBe duration
                    worker.luxelCountPerThread shouldBe luxelCount
                }
            }

            it("creates a Spawn worker") {
                checkAll(
                    Exhaustive.enum<Resolution>(),
                    Arb.int(0, 1024),
                    durationArb(),
                    Arb.long(1024L, 65536L),
                ) { resolution, frameIndex, duration, luxelCount ->

                    val configuration = instantiation {
                        simulation { passType(PassType.SPAWN) }
                        render { resolution(resolution) }
                    }

                    val stubSimulator = StubSimulator<D, I>()
                    val stubFilm = StubFilm(resolution)
                    val stubProjection = StubProjection<D>()
                    val logger = Logger(StubLogHandler())
                    val frameInfo = FrameInfo(frameIndex, duration)
                    val worker = configuration.createWorker(
                        stubSimulator,
                        stubFilm,
                        frameInfo,
                        luxelCount,
                        stubProjection,
                        logger,
                    ) as AbstractSimulationWorker<*, *, *>

                    (worker is SpawnSimulationWorker<*, *, *>) shouldBe true
                    worker.film shouldBeSameInstanceAs stubFilm
                    worker.simulator shouldBeSameInstanceAs stubSimulator
                    worker.simulation shouldBeSameInstanceAs configuration.simulation
                    worker.projection shouldBeSameInstanceAs stubProjection
                    worker.logger shouldBeSameInstanceAs logger
                    worker.time shouldBe duration
                    worker.luxelCountPerThread shouldBe luxelCount
                }
            }
        }

        describe("render") {
            it("has sensible defaults") {
                val configuration = instantiation {}

                configuration.render.filmSpace shouldBe Volume(Vector2(0.0, 0.0), Vector2(1024.0, 768.0))
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
                val film = configuration.render.createFilm()

                configuration.render.filmType shouldBe FilmType.CLEAN
                (film is CleanFilm) shouldBe true
            }

            it("sets the film type to rough") {
                val configuration = instantiation {
                    render {
                        filmType(FilmType.ROUGH)
                    }
                }
                val film = configuration.render.createFilm()

                configuration.render.filmType shouldBe FilmType.ROUGH
                (film is RoughFilm) shouldBe true
            }

            it("sets the resolution") {
                checkAll(Exhaustive.enum<Resolution>()) { resolution ->
                    val configuration = instantiation {
                        render {
                            resolution(resolution)
                        }
                    }
                    val film = configuration.render.createFilm()

                    configuration.render.resolution shouldBe resolution
                    configuration.render.filmSpace shouldBe Volume(
                        min = Vector2(0.0, 0.0),
                        max = Vector2(resolution.width.toDouble(), resolution.height.toDouble()),
                    )
                    film.width shouldBe resolution.width
                    film.height shouldBe resolution.height
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
                configuration.animation.frameCount shouldBe 1
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
                    configuration.animation.frameCount shouldBe ((10 * fps) + 1)
                }
            }

            it("start with frame 0") {
                checkAll(durationArb(), Arb.int(1, 1024)) { duration, fps ->
                    val configuration = instantiation {
                        animation {
                            duration(duration)
                            fps(fps)
                        }
                    }
                    val initialFrame = configuration.animation.frameInfo

                    initialFrame.frameIndex shouldBe 0
                    initialFrame.frameTime shouldBe 0.seconds
                }
            }

            it("increment frames") {
                checkAll(Arb.int(1), Arb.int(1, 1024)) { durationSec, fps ->
                    val duration = durationSec.seconds
                    val configuration = instantiation {
                        animation {
                            duration(duration)
                            fps(fps)
                        }
                    }
                    val frameCount = configuration.animation.frameCount
                    val increment = max(128, frameCount / 2)
                    repeat(increment) {
                        configuration.animation.increment()
                    }
                    val frameInfo = configuration.animation.frameInfo

                    frameInfo.frameIndex shouldBe increment
                    frameInfo.frameTime shouldBe (configuration.animation.timeStep * increment)
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
                        simulationSpaceDensity(density)
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
