package fr.xgouchet.luxels.engine.simulation

import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.log.StdOutLogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.simulation.runner.SceneAnimationRunner
import fr.xgouchet.luxels.engine.simulation.runner.SingleSimulationRunner
import fr.xgouchet.luxels.engine.test.kotest.property.commonConfigurationArb
import fr.xgouchet.luxels.engine.test.kotest.property.dimensionArb
import fr.xgouchet.luxels.engine.test.kotest.property.inputSourceArb
import fr.xgouchet.luxels.engine.test.kotest.property.sceneConfigurationArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.kotest.matchers.types.beOfType
import io.kotest.property.Arb
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class LuxelEngineSpec : DescribeSpec(
    {
        describe("constructor") {
            it("uses sensible defaults") {
                checkAll(dimensionArb()) { dimension ->
                    val defaultEngine = LuxelEngine(dimension)

                    defaultEngine.logHandler should beOfType(StdOutLogHandler::class)
                    defaultEngine.simulationRunner should beOfType(SceneAnimationRunner::class)
                }
            }
        }

        describe("runSimulation") {
            it("runs a simulation for each input") {
                checkAll(
                    sceneConfigurationArb(),
                    commonConfigurationArb(),
                    inputSourceArb(),
                    Arb.string(),
                ) { sceneConfig, commonConfig, inputSource, name ->
                    val logHandler = mock<LogHandler>()
                    val scene = mock<Scene<Dimension, Luxel<Dimension>, Long, Environment<Dimension>>> {
                        every { outputName() } returns (name)
                    }

                    val sceneRunner = mock<SingleSimulationRunner<Dimension>>()
                    val testedEngine = LuxelEngine(sceneConfig.dimension, logHandler, sceneRunner)

                    testedEngine.runSimulation(scene, commonConfig, inputSource, sceneConfig.simulationVolume)

                    inputSource.forEachIndexed { idx, input ->
                        verifySuspend {
                            sceneRunner.runSimulation(scene, commonConfig, input, sceneConfig.simulationVolume)
                        }
                    }
                }
            }
        }
    },
)
