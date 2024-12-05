package fr.xgouchet.luxels.core.simulation

import fr.xgouchet.luxels.core.configuration.configurationWithRandomSeeds
import fr.xgouchet.luxels.core.log.Log
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.render.FrameInfo
import fr.xgouchet.luxels.core.simulation.MultiFrameSimulationRunner
import fr.xgouchet.luxels.core.test.stub.StubFrameRunner
import fr.xgouchet.luxels.core.test.stub.StubLogHandler
import fr.xgouchet.luxels.core.test.stub.StubSimulator
import fr.xgouchet.luxels.core.test.stub.core.verifyCall
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import kotlin.time.Duration.Companion.seconds

class MultiFrameSimulationRunnerSpec : DescribeSpec(
    {
        describe("a multi-frame sim runner") {
            it("resets the simulator environment") {
                checkAll(Arb.int(1, 200), Arb.int(1, 100), Arb.int(1, 100)) { fps, frameCount, threadCount ->
                    val frameStep = 1.seconds / fps
                    val stubLogHandler = StubLogHandler()
                    val stubFrameRunner = StubFrameRunner()
                    val stubSimulator = StubSimulator<Dimension.D3, Long>()
                    val stubConfig = configurationWithRandomSeeds(Dimension.D3) {
                        animation {
                            duration(frameStep * frameCount)
                            fps(fps)
                        }
                    }
                    val stubInput = stubConfig.input.source.first()
                    val testedRunner = MultiFrameSimulationRunner(stubLogHandler, stubFrameRunner)

                    testedRunner.runSimulationWithInput(
                        stubSimulator,
                        stubConfig,
                        threadCount,
                        stubInput,
                    )

                    stubSimulator.verifyCall(
                        "initEnvironment",
                        mapOf(
                            "simulation" to stubConfig.simulation,
                            "inputData" to stubInput,
                            "logHandler" to stubLogHandler,
                        ),
                    )
                }
            }

            it("resets the RndGen seed") {
                checkAll(Arb.int(1, 200), Arb.int(1, 100), Arb.int(1, 100)) { fps, frameCount, threadCount ->
                    val frameStep = 1.seconds / fps
                    val stubLogHandler = StubLogHandler()
                    val stubFrameRunner = StubFrameRunner()
                    val stubSimulator = StubSimulator<Dimension.D3, Long>()
                    val stubConfig = configurationWithRandomSeeds(Dimension.D3) {
                        animation {
                            duration(frameStep * frameCount)
                            fps(fps)
                        }
                    }
                    val stubInput = stubConfig.input.source.first()
                    val rndSeedBefore = RndGen.seed
                    val testedRunner = MultiFrameSimulationRunner(stubLogHandler, stubFrameRunner)

                    testedRunner.runSimulationWithInput(
                        stubSimulator,
                        stubConfig,
                        threadCount,
                        stubInput,
                    )

                    RndGen.seed shouldNotBe rndSeedBefore
                    RndGen.seed shouldBe stubInput.seed
                }
            }

            it("simulate all frames") {
                checkAll(Arb.int(1, 200), Arb.int(1, 100), Arb.int(1, 100)) { fps, frameCount, threadCount ->
                    val frameStep = 1.seconds / fps
                    val stubLogHandler = StubLogHandler()
                    val stubFrameRunner = StubFrameRunner()
                    val stubSimulator = StubSimulator<Dimension.D3, Long>()
                    val stubConfig = configurationWithRandomSeeds(Dimension.D3) {
                        animation {
                            duration(frameStep * frameCount)
                            fps(fps)
                        }
                    }
                    val stubInput = stubConfig.input.source.first()
                    val testedRunner = MultiFrameSimulationRunner(stubLogHandler, stubFrameRunner)

                    testedRunner.runSimulationWithInput(
                        stubSimulator,
                        stubConfig,
                        threadCount,
                        stubInput,
                    )

                    repeat(frameCount) { idx ->
                        val frameInfo = FrameInfo(idx, frameStep * idx)
                        stubLogHandler.verifyCall("onLog", mapOf("log" to Log.StartSection("FRAME $frameInfo")))
                        stubFrameRunner.verifyCall(
                            "simulateFrame",
                            mapOf(
                                "simulator" to stubSimulator,
                                "configuration" to stubConfig,
                                "threadCount" to threadCount,
                                "inputData" to stubInput,
                                "frameInfo" to frameInfo
                            ),
                        )
                        stubLogHandler.verifyCall("onLog", mapOf("log" to Log.EndSection))
                    }
                }
            }
        }
    },
)