package fr.xgouchet.luxels.core.engine

import fr.xgouchet.luxels.core.configuration.configurationWithRandomSeeds
import fr.xgouchet.luxels.core.log.Log
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.test.stub.StubLogHandler
import fr.xgouchet.luxels.core.test.stub.StubSimulationRunner
import fr.xgouchet.luxels.core.test.stub.StubSimulator
import fr.xgouchet.luxels.core.test.stub.core.verifyCall
import io.kotest.core.spec.style.DescribeSpec

class LuxelEngineSpec : DescribeSpec(
    {
        describe("running a simulation") {
            it("uses the provided simulator with all input data") {
                val stubLogHandler = StubLogHandler()
                val stubSimulator = StubSimulator<Dimension.D3, Long>()
                val stubConfig = configurationWithRandomSeeds(Dimension.D3) {}
                val stubSimulationRunner = StubSimulationRunner()
                val testedEngine = LuxelEngine(stubLogHandler, stubSimulationRunner)

                testedEngine.runSimulation(stubSimulator, stubConfig)

                stubConfig.input.source.forEach { inputData ->
                    stubSimulationRunner.verifyCall(
                        "runSimulationWithInput",
                        mapOf(
                            "simulator" to stubSimulator,
                            "configuration" to stubConfig,
                            "threadCount" to stubConfig.simulation.maxThreadCount,
                            "inputData" to inputData,
                        ),
                    )
                }
            }

            it("reports a log section for each input") {
                val stubLogHandler = StubLogHandler()
                val stubSimulator = StubSimulator<Dimension.D3, Long>()
                val stubConfig = configurationWithRandomSeeds(Dimension.D3) {}
                val stubSimulationRunner = StubSimulationRunner()
                val testedEngine = LuxelEngine(stubLogHandler, stubSimulationRunner)

                testedEngine.runSimulation(stubSimulator, stubConfig)

                stubConfig.input.source.forEach { inputData ->
                    stubLogHandler.verifyCall(
                        "onLog",
                        mapOf("log" to Log.StartSection("Running simulation with input ${inputData.id}")),
                    )
                    stubLogHandler.verifyCall(
                        "onLog",
                        mapOf("log" to Log.EndSection),
                    )
                }
            }
        }
    },
)
