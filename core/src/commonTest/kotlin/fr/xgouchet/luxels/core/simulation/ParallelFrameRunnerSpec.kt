package fr.xgouchet.luxels.core.simulation

import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configurationWithRandomSeeds
import fr.xgouchet.luxels.core.log.Log
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.test.kotest.property.frameInfoArb
import fr.xgouchet.luxels.core.test.stub.StubLogHandler
import fr.xgouchet.luxels.core.test.stub.StubSimulationWorker
import fr.xgouchet.luxels.core.test.stub.StubSimulator
import fr.xgouchet.luxels.core.test.stub.StubWorkerProvider
import fr.xgouchet.luxels.core.test.stub.core.stubResponse
import fr.xgouchet.luxels.core.test.stub.core.verifyCall
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class ParallelFrameRunnerSpec : DescribeSpec(
    {
        describe("a parallel frame runner") {
            it("calls simulator lifecycle methods") {
                checkAll(Arb.Companion.int(1, 16), frameInfoArb()) { threadCount, frameInfo ->
                    val stubLogHandler = StubLogHandler()
                    val stubSimulator = StubSimulator<Dimension.D3, Long>()
                    val stubConfig = configurationWithRandomSeeds(Dimension.D3) {
                        render { resolution(Resolution.QVGA) }
                    }
                    val stubInput = stubConfig.input.source.first()
                    val stubWorkerProvider = StubWorkerProvider()
                    stubWorkerProvider.stubResponse("createWorker").withValue(StubSimulationWorker())

                    val testedRunner = ParallelFrameRunner(
                        stubLogHandler,
                        stubWorkerProvider,
                    )

                    testedRunner.simulateFrame(
                        stubSimulator,
                        stubConfig,
                        threadCount,
                        stubInput,
                        frameInfo,
                    )

                    stubSimulator.verifyCall("onFrameStart")
                    stubSimulator.verifyCall("onFrameEnd")
                }
            }

            it("calls loghandler progress start and stop") {
                checkAll(Arb.Companion.int(1, 16), frameInfoArb()) { threadCount, frameInfo ->
                    val stubLogHandler = StubLogHandler()
                    val stubSimulator = StubSimulator<Dimension.D3, Long>()
                    val stubConfig = configurationWithRandomSeeds(Dimension.D3) {
                        render { resolution(Resolution.QVGA) }
                    }
                    val stubInput = stubConfig.input.source.first()
                    val stubWorkerProvider = StubWorkerProvider()
                    stubWorkerProvider.stubResponse("createWorker").withValue(StubSimulationWorker())

                    val testedRunner = ParallelFrameRunner(stubLogHandler, stubWorkerProvider)

                    testedRunner.simulateFrame(
                        stubSimulator,
                        stubConfig,
                        threadCount,
                        stubInput,
                        frameInfo,
                    )

                    stubLogHandler.verifyCall("onLog", mapOf("log" to Log.StartProgress))
                    stubLogHandler.verifyCall("onLog", mapOf("log" to Log.EndProgress))
                }
            }

            it("create a new runner per thread") {
                checkAll(Arb.Companion.int(1, 16), frameInfoArb()) { threadCount, frameInfo ->

                    val stubLogHandler = StubLogHandler()
                    val stubSimulator = StubSimulator<Dimension.D3, Long>()
                    val stubConfig = configurationWithRandomSeeds(Dimension.D3) {
                        render { resolution(Resolution.QVGA) }
                    }
                    val stubInput = stubConfig.input.source.first()
                    val stubWorkerProvider = StubWorkerProvider()
                    val stubWorkers = (0 until threadCount).map { StubSimulationWorker() }
                    stubWorkerProvider.stubResponse("createWorker").withValues(*stubWorkers.toTypedArray())

                    val testedRunner = ParallelFrameRunner(stubLogHandler, stubWorkerProvider)

                    testedRunner.simulateFrame(
                        stubSimulator,
                        stubConfig,
                        threadCount,
                        stubInput,
                        frameInfo,
                    )

                    stubWorkers.forEach { runner ->
                        runner.verifyCall("work", mapOf())
                    }
                }
            }
        }
    },
)
