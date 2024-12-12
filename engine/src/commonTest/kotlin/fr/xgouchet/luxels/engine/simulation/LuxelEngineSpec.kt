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
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import fr.xgouchet.luxels.engine.simulation.runner.SceneAnimationRunner
import fr.xgouchet.luxels.engine.simulation.runner.SimulationRunner
import fr.xgouchet.luxels.engine.test.kotest.property.configurationArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.kotest.matchers.types.beOfType
import io.kotest.property.Arb
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

class LuxelEngineSpec : DescribeSpec(
    {
        describe("constructor") {
            it("uses sensible defaults") {
                val defaultEngine = LuxelEngine()

                defaultEngine.logHandler should beOfType(StdOutLogHandler::class)
                defaultEngine.simulationRunner should beOfType(SceneAnimationRunner::class)
            }
        }

        describe("runSimulation") {
            it("runs a simulation for each input") {
                checkAll(configurationArb(), Arb.string()) { configuration, name ->
                    val logHandler = mock<LogHandler>()
                    val scene = mock<Scene<Dimension, Luxel<Dimension>, Long, Environment<Dimension>>> {
                        every { outputName() } returns (name)
                    }

                    val sceneRunner = mock<SimulationRunner>()
                    val testedEngine = LuxelEngine(logHandler, sceneRunner)

                    testedEngine.runSimulation(scene, configuration)

                    configuration.input.source.forEachIndexed { idx, input ->
                        val expectedConfiguration = InternalConfiguration<Dimension, Long>(
                            dimension = configuration.dimension,
                            inputData = input,
                            simulationVolume = configuration.simulation.volume,
                            simulationLuxelCount = configuration.simulation.quality.count,
                            simulationMaxThreadCount = configuration.simulation.maxThreadCount,
                            simulationType = configuration.simulation.simulationType,
                            animationDuration = configuration.animation.duration,
                            animationFrameStep = 1.seconds / configuration.animation.fps,
                            animationFrameInfo = FrameInfo(0, 0.nanoseconds),
                            outputFilmType = configuration.render.filmType,
                            outputResolution = configuration.render.resolution,
                            outputFixer = configuration.render.fixer,
                        )
                        verifySuspend { sceneRunner.runSimulation(scene, expectedConfiguration) }
                    }
                }
            }
        }
    },
)
