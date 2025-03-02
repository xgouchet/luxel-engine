package art.luxels.engine.simulation.runner

import art.luxels.core.log.Log
import art.luxels.core.log.LogHandler
import art.luxels.core.math.Dimension
import art.luxels.core.math.random.RndGen
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Luxel
import art.luxels.engine.api.Scene
import art.luxels.engine.render.Projection
import art.luxels.engine.simulation.SimulationContext
import art.luxels.engine.test.kotest.property.commonConfigurationArb
import art.luxels.engine.test.kotest.property.dimensionArb
import art.luxels.engine.test.kotest.property.inputSourceArb
import art.luxels.engine.test.kotest.property.sceneConfigurationArb
import art.luxels.engine.test.kotest.property.shortDurationArb
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beOfType
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import kotlin.time.Duration.Companion.nanoseconds

class SceneAnimationRunnerSpec : DescribeSpec(
    {
        describe("constructor") {
            it("uses sensible defaults") {
                checkAll(dimensionArb()) { dimension ->
                    val logHandler = mock<LogHandler>()
                    val defaultRunner = SceneAnimationRunner(dimension, logHandler)

                    defaultRunner.simulationRunner should beOfType(ParallelSimulationRunner::class)
                }
            }
        }

        describe("runSimulation") {
            it("resets the RndGen seed") {
                checkAll(
                    sceneConfigurationArb(),
                    commonConfigurationArb(),
                    inputSourceArb(),
                ) { sceneConfig, commonConfig, inputSource ->
                    val logHandler = mock<LogHandler>()
                    val simulationRunner = mock<SimulationRunner>()
                    val environment = mock<Environment<Dimension>>()
                    val projection = mock<Projection<Dimension>>()
                    val scene = mock<Scene<Dimension, Luxel<Dimension>, Long, Environment<Dimension>>> {
                        every {
                            getEnvironment(
                                sceneConfig.simulationVolume,
                                sceneConfig.inputData,
                                commonConfig.animationDuration,
                            )
                        } returns environment
                        every {
                            getProjection(
                                sceneConfig.simulationVolume,
                                commonConfig.outputResolution.asVector2().asVolume(),
                                any<FrameInfo>(),
                            )
                        } returns projection
                    }
                    val testedRunner = SceneAnimationRunner(sceneConfig.dimension, logHandler, simulationRunner)

                    testedRunner.runSimulation(scene, commonConfig, sceneConfig.inputData, sceneConfig.simulationVolume)

                    RndGen.seed shouldBe sceneConfig.inputData.seed
                }
            }

            it("simulate all frames") {
                checkAll(sceneConfigurationArb(), commonConfigurationArb()) { sceneConfig, commonConfig ->
                    val logHandler = mock<LogHandler>()
                    val simulationRunner = mock<SimulationRunner>()
                    val environment = mock<Environment<Dimension>>()
                    val projection = mock<Projection<Dimension>>()
                    val scene = mock<Scene<Dimension, Luxel<Dimension>, Long, Environment<Dimension>>> {
                        every {
                            getEnvironment(
                                sceneConfig.simulationVolume,
                                sceneConfig.inputData,
                                commonConfig.animationDuration,
                            )
                        } returns environment
                        every {
                            getProjection(
                                sceneConfig.simulationVolume,
                                commonConfig.outputResolution.asVector2().asVolume(),
                                any<FrameInfo>(),
                            )
                        } returns projection
                    }
                    val frameCount = (commonConfig.animationDuration / commonConfig.animationFrameStep).toInt()
                    val testedRunner = SceneAnimationRunner(sceneConfig.dimension, logHandler, simulationRunner)

                    testedRunner.runSimulation(scene, commonConfig, sceneConfig.inputData, sceneConfig.simulationVolume)

                    repeat(frameCount) { idx ->
                        val frameTime = commonConfig.animationFrameStep * idx
                        val progress = frameTime / commonConfig.animationDuration
                        val frameInfo = FrameInfo(idx, frameTime, progress)
                        val expectedSceneConfig = sceneConfig.copy(context = SimulationContext(environment, projection))
                        val expectedCommonConfig = commonConfig.copy(animationFrameInfo = frameInfo)
                        verifySuspend {
                            simulationRunner.runSimulation(
                                scene,
                                expectedSceneConfig,
                                expectedCommonConfig,
                            )
                        }
                    }
                }
            }

            it("reports a log section for each frame") {
                checkAll(sceneConfigurationArb(), commonConfigurationArb()) { sceneConfig, commonConfig ->
                    val logHandler = mock<LogHandler>()
                    val simulationRunner = mock<SimulationRunner>()
                    val environment = mock<Environment<Dimension>>()
                    val projection = mock<Projection<Dimension>>()
                    val scene = mock<Scene<Dimension, Luxel<Dimension>, Long, Environment<Dimension>>> {
                        every {
                            getEnvironment(
                                sceneConfig.simulationVolume,
                                sceneConfig.inputData,
                                commonConfig.animationDuration,
                            )
                        } returns environment
                        every {
                            getProjection(
                                sceneConfig.simulationVolume,
                                commonConfig.outputResolution.asVector2().asVolume(),
                                any<FrameInfo>(),
                            )
                        } returns projection
                    }

                    val frameCount = (commonConfig.animationDuration / commonConfig.animationFrameStep).toInt()
                    val testedRunner = SceneAnimationRunner(sceneConfig.dimension, logHandler, simulationRunner)

                    testedRunner.runSimulation(scene, commonConfig, sceneConfig.inputData, sceneConfig.simulationVolume)

                    repeat(frameCount) { idx ->
                        val progress = idx.toDouble() / frameCount
                        val frameInfo = FrameInfo(idx, commonConfig.animationFrameStep * idx, progress)

                        verify { logHandler.onLog(Log.StartSection("Frame $frameInfo")) }
                    }
                }
            }
        }

        describe("increment") {

            it("increment a frame") {
                checkAll(
                    commonConfigurationArb(),
                    dimensionArb(),
                    shortDurationArb(),
                    Arb.Companion.int(64, 128),
                    Arb.Companion.int(0, 32),
                ) { baseConfig, dimension, frameStep, frameCount, frameIdx ->
                    val logHandler = mock<LogHandler>()
                    val simulationRunner = mock<SimulationRunner>()
                    val progress = frameIdx.toDouble() / frameCount
                    val frameInfo = FrameInfo(frameIdx, frameStep * frameIdx, progress)
                    val expectedIdx = frameIdx + 1
                    val frameStepNs = frameStep.inWholeNanoseconds.toDouble()
                    val expectedProgress = (expectedIdx * frameStepNs) / ((frameCount * frameStepNs) + 1)
                    val expectedFrameInfo = FrameInfo(expectedIdx, frameStep * expectedIdx, expectedProgress)
                    val testedRunner = SceneAnimationRunner(dimension, logHandler, simulationRunner)
                    val configuration = baseConfig.copy(
                        animationDuration = (frameStep * frameCount) + 1.nanoseconds,
                        animationFrameStep = frameStep,
                    )

                    val result = testedRunner.increment(frameInfo, configuration)

                    result shouldBe expectedFrameInfo
                }
            }

            it("return null when animation finished") {
                checkAll(
                    commonConfigurationArb(),
                    dimensionArb(),
                    shortDurationArb(),
                    Arb.Companion.int(min = 0, max = 65536),
                ) { baseConfig, dimension, frameStep, frameCount ->
                    val logHandler = mock<LogHandler>()
                    val simulationRunner = mock<SimulationRunner>()
                    val frameIdx = frameCount + 1
                    val frameInfo = FrameInfo(frameIdx, frameStep * frameIdx, 1.0)
                    val configuration = baseConfig.copy(
                        animationDuration = (frameStep * frameCount) + 1.nanoseconds,
                        animationFrameStep = frameStep,
                    )
                    val testedRunner = SceneAnimationRunner(dimension, logHandler, simulationRunner)

                    val result = testedRunner.increment(frameInfo, configuration)

                    result shouldBe null
                }
            }
        }
    },
)
