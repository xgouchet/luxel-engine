package fr.xgouchet.luxels.engine.simulation.runner

import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import fr.xgouchet.luxels.core.log.Log
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.test.kotest.property.internalConfigurationArb
import fr.xgouchet.luxels.engine.test.kotest.property.shortDurationArb
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
                val logHandler = mock<LogHandler>()
                val defaultRunner = SceneAnimationRunner(logHandler)

                defaultRunner.simulationRunner should beOfType(ParallelSimulationRunner::class)
            }
        }

        describe("runSimulation") {
            it("resets the RndGen seed") {
                checkAll(internalConfigurationArb()) { configuration ->
                    val logHandler = mock<LogHandler>()
                    val simulationRunner = mock<SimulationRunner>()
                    val scene = mock<Scene<Dimension, Luxel<Dimension>, Long, Environment<Dimension>>>()
                    val testedRunner = SceneAnimationRunner(logHandler, simulationRunner)

                    testedRunner.runSimulation(scene, configuration)

                    RndGen.seed shouldBe configuration.inputData.seed
                }
            }

            it("simulate all frames") {
                checkAll(internalConfigurationArb()) { configuration ->
                    val logHandler = mock<LogHandler>()
                    val simulationRunner = mock<SimulationRunner>()
                    val scene = mock<Scene<Dimension, Luxel<Dimension>, Long, Environment<Dimension>>>()
                    val frameCount = (
                        configuration.animationDuration.inWholeNanoseconds /
                            configuration.animationFrameStep.inWholeNanoseconds
                        ).toInt()
                    val testedRunner = SceneAnimationRunner(logHandler, simulationRunner)

                    testedRunner.runSimulation(scene, configuration)

                    repeat(frameCount) { idx ->
                        val frameInfo = FrameInfo(idx, configuration.animationFrameStep * idx)
                        val expectedConfiguration = configuration.copy(animationFrameInfo = frameInfo)
                        verifySuspend { simulationRunner.runSimulation(scene, expectedConfiguration) }
                    }
                }
            }

            it("reports a log section for each frame") {
                checkAll(internalConfigurationArb()) { configuration ->
                    val logHandler = mock<LogHandler>()
                    val simulationRunner = mock<SimulationRunner>()
                    val scene = mock<Scene<Dimension, Luxel<Dimension>, Long, Environment<Dimension>>>()
                    val frameCount = (
                        configuration.animationDuration.inWholeNanoseconds /
                            configuration.animationFrameStep.inWholeNanoseconds
                        ).toInt()
                    val testedRunner = SceneAnimationRunner(logHandler, simulationRunner)

                    testedRunner.runSimulation(scene, configuration)

                    repeat(frameCount) { idx ->
                        val frameInfo = FrameInfo(idx, configuration.animationFrameStep * idx)

                        verify { logHandler.onLog(Log.StartSection("Frame $frameInfo")) }
                    }
                }
            }
        }

        describe("increment") {

            it("increment a frame") {
                checkAll(
                    internalConfigurationArb(),
                    shortDurationArb(),
                    Arb.Companion.int(64, 128),
                    Arb.Companion.int(0, 32),
                ) { baseConfig, frameStep, frameCount, frameIdx ->
                    val logHandler = mock<LogHandler>()
                    val simulationRunner = mock<SimulationRunner>()
                    val frameInfo = FrameInfo(frameIdx, frameStep * frameIdx)
                    val expectedIdx = frameIdx + 1
                    val expectedFrameInfo = FrameInfo(expectedIdx, frameStep * expectedIdx)
                    val testedRunner = SceneAnimationRunner(logHandler, simulationRunner)
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
                    internalConfigurationArb(),
                    shortDurationArb(),
                    Arb.Companion.int(min = 0, max = 65536),
                ) { baseConfig, frameStep, frameCount ->
                    val logHandler = mock<LogHandler>()
                    val simulationRunner = mock<SimulationRunner>()
                    val frameIdx = frameCount + 1
                    val frameInfo = FrameInfo(frameIdx, frameStep * frameIdx)
                    val configuration = baseConfig.copy(
                        animationDuration = (frameStep * frameCount) + 1.nanoseconds,
                        animationFrameStep = frameStep,
                    )
                    val testedRunner = SceneAnimationRunner(logHandler, simulationRunner)

                    val result = testedRunner.increment(frameInfo, configuration)

                    result shouldBe null
                }
            }
        }
    },
)
