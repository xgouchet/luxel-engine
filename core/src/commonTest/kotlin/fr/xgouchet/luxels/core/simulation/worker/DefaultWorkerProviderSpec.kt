package fr.xgouchet.luxels.core.simulation.worker

import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configuration
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.render.FrameInfo
import fr.xgouchet.luxels.core.test.kotest.property.durationArb
import fr.xgouchet.luxels.core.test.stub.StubFilm
import fr.xgouchet.luxels.core.test.stub.StubLogHandler
import fr.xgouchet.luxels.core.test.stub.StubProjection
import fr.xgouchet.luxels.core.test.stub.StubSimulator
import fr.xgouchet.luxels.core.test.stub.core.stubResponse
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.enum

class DefaultWorkerProviderSpec : DescribeSpec(
    {
        describe("a worker provider") {
            it("creates a Render worker") {
                checkAll(
                    Exhaustive.enum<Resolution>(),
                    Arb.int(0, 1024),
                    durationArb(),
                    Arb.long(1024L, 65536L),
                ) { resolution, frameIndex, duration, luxelCount ->
                    val stubSimulator = StubSimulator<Dimension.D3, Unit>()
                    val stubFilm = StubFilm(resolution)
                    val stubProjection = StubProjection<Dimension.D3>()
                    val stubConfig = configuration(Dimension.D3) {
                        simulation { passType(PassType.RENDER) }
                        render { resolution(resolution) }
                    }
                    stubSimulator.stubResponse("getProjection").withValue(stubProjection)
                    val stubLogHandler = StubLogHandler()
                    val frameInfo = FrameInfo(frameIndex, duration)

                    val worker = DefaultWorkerProvider().createWorker(
                        stubSimulator,
                        stubFilm,
                        frameInfo,
                        luxelCount,
                        stubConfig,
                        stubLogHandler,
                    ) as AbstractSimulationWorker<*, *, *>

                    (worker is RenderSimulationWorker<*, *, *>) shouldBe true
                    worker.film shouldBeSameInstanceAs stubFilm
                    worker.simulator shouldBeSameInstanceAs stubSimulator
                    worker.simulation shouldBeSameInstanceAs stubConfig.simulation
                    worker.projection shouldBeSameInstanceAs stubProjection
                    worker.logHandler shouldBeSameInstanceAs stubLogHandler
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
                    val stubSimulator = StubSimulator<Dimension.D3, Unit>()
                    val stubFilm = StubFilm(resolution)
                    val stubProjection = StubProjection<Dimension.D3>()
                    val stubConfig = configuration(Dimension.D3) {
                        simulation { passType(PassType.ENV) }
                        render { resolution(resolution) }
                    }
                    stubSimulator.stubResponse("getProjection").withValue(stubProjection)
                    val stubLogHandler = StubLogHandler()
                    val frameInfo = FrameInfo(frameIndex, duration)

                    val worker = DefaultWorkerProvider().createWorker(
                        stubSimulator,
                        stubFilm,
                        frameInfo,
                        luxelCount,
                        stubConfig,
                        stubLogHandler,
                    ) as AbstractSimulationWorker<*, *, *>

                    (worker is EnvSimulationWorker<*, *, *>) shouldBe true
                    worker.film shouldBeSameInstanceAs stubFilm
                    worker.simulator shouldBeSameInstanceAs stubSimulator
                    worker.simulation shouldBeSameInstanceAs stubConfig.simulation
                    worker.projection shouldBeSameInstanceAs stubProjection
                    worker.logHandler shouldBeSameInstanceAs stubLogHandler
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
                    val stubSimulator = StubSimulator<Dimension.D3, Unit>()
                    val stubFilm = StubFilm(resolution)
                    val stubProjection = StubProjection<Dimension.D3>()
                    val stubConfig = configuration(Dimension.D3) {
                        simulation { passType(PassType.DEATH) }
                        render { resolution(resolution) }
                    }
                    stubSimulator.stubResponse("getProjection").withValue(stubProjection)
                    val stubLogHandler = StubLogHandler()
                    val frameInfo = FrameInfo(frameIndex, duration)

                    val worker = DefaultWorkerProvider().createWorker(
                        stubSimulator,
                        stubFilm,
                        frameInfo,
                        luxelCount,
                        stubConfig,
                        stubLogHandler,
                    ) as AbstractSimulationWorker<*, *, *>

                    (worker is DeathSimulationWorker<*, *, *>) shouldBe true
                    worker.film shouldBeSameInstanceAs stubFilm
                    worker.simulator shouldBeSameInstanceAs stubSimulator
                    worker.simulation shouldBeSameInstanceAs stubConfig.simulation
                    worker.projection shouldBeSameInstanceAs stubProjection
                    worker.logHandler shouldBeSameInstanceAs stubLogHandler
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
                    val stubSimulator = StubSimulator<Dimension.D3, Unit>()
                    val stubFilm = StubFilm(resolution)
                    val stubProjection = StubProjection<Dimension.D3>()
                    val stubConfig = configuration(Dimension.D3) {
                        simulation { passType(PassType.PATH) }
                        render { resolution(resolution) }
                    }
                    stubSimulator.stubResponse("getProjection").withValue(stubProjection)
                    val stubLogHandler = StubLogHandler()
                    val frameInfo = FrameInfo(frameIndex, duration)

                    val worker = DefaultWorkerProvider().createWorker(
                        stubSimulator,
                        stubFilm,
                        frameInfo,
                        luxelCount,
                        stubConfig,
                        stubLogHandler,
                    ) as AbstractSimulationWorker<*, *, *>

                    (worker is PathSimulationWorker<*, *, *>) shouldBe true
                    worker.film shouldBeSameInstanceAs stubFilm
                    worker.simulator shouldBeSameInstanceAs stubSimulator
                    worker.simulation shouldBeSameInstanceAs stubConfig.simulation
                    worker.projection shouldBeSameInstanceAs stubProjection
                    worker.logHandler shouldBeSameInstanceAs stubLogHandler
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
                    val stubSimulator = StubSimulator<Dimension.D3, Unit>()
                    val stubFilm = StubFilm(resolution)
                    val stubProjection = StubProjection<Dimension.D3>()
                    val stubConfig = configuration(Dimension.D3) {
                        simulation { passType(PassType.SPAWN) }
                        render { resolution(resolution) }
                    }
                    stubSimulator.stubResponse("getProjection").withValue(stubProjection)
                    val stubLogHandler = StubLogHandler()
                    val frameInfo = FrameInfo(frameIndex, duration)

                    val worker = DefaultWorkerProvider().createWorker(
                        stubSimulator,
                        stubFilm,
                        frameInfo,
                        luxelCount,
                        stubConfig,
                        stubLogHandler,
                    ) as AbstractSimulationWorker<*, *, *>

                    (worker is SpawnSimulationWorker<*, *, *>) shouldBe true
                    worker.film shouldBeSameInstanceAs stubFilm
                    worker.simulator shouldBeSameInstanceAs stubSimulator
                    worker.simulation shouldBeSameInstanceAs stubConfig.simulation
                    worker.projection shouldBeSameInstanceAs stubProjection
                    worker.logHandler shouldBeSameInstanceAs stubLogHandler
                    worker.time shouldBe duration
                    worker.luxelCountPerThread shouldBe luxelCount
                }
            }
        }
    },
)
