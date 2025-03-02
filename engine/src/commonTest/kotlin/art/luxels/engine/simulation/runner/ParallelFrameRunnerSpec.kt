package art.luxels.engine.simulation.runner

import art.luxels.core.log.LogHandler
import art.luxels.core.math.Dimension
import art.luxels.core.render.Film
import art.luxels.core.render.Resolution
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Luxel
import art.luxels.engine.api.Scene
import art.luxels.engine.api.Simulator
import art.luxels.engine.render.DefaultFilmProvider
import art.luxels.engine.render.FilmProvider
import art.luxels.engine.simulation.worker.DefaultWorkerProvider
import art.luxels.engine.simulation.worker.SimulationWorker
import art.luxels.engine.simulation.worker.WorkerProvider
import art.luxels.engine.test.kotest.property.commonConfigurationArb
import art.luxels.engine.test.kotest.property.sceneConfigurationArb
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.kotest.matchers.types.beOfType
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class ParallelFrameRunnerSpec : DescribeSpec(
    {
        describe("constructor") {
            it("uses sensible defaults") {
                val logHandler = mock<LogHandler>()
                val defaultRunner = ParallelSimulationRunner(logHandler)

                defaultRunner.filmProvider should beOfType(DefaultFilmProvider::class)
                defaultRunner.workerProvider should beOfType(DefaultWorkerProvider::class)
                defaultRunner.threadCountComputer should beOfType(DefaultThreadCountComputer::class)
            }
        }

        describe("runSimulation") {
            it("run on multiple threads") {
                checkAll(
                    sceneConfigurationArb(),
                    commonConfigurationArb(),
                    Arb.int(1, 3),
                ) { sceneConfig, baseCommonConfig, threadCount ->
                    val resolution = Resolution.SQUARE_128
                    val commonConfig = baseCommonConfig.copy(outputResolution = resolution)
                    val logHandler = mock<LogHandler>()
                    val expectedWorkerConfig = commonConfig.copy(
                        simulationLuxelCount = commonConfig.simulationLuxelCount / threadCount,
                    )
                    val films = List(threadCount) {
                        mock<Film> {
                            every { width } returns resolution.width
                            every { height } returns resolution.height
                            every { hasData() } returns false
                        }
                    }
                    val simulators = List(threadCount) {
                        mock<Simulator<Dimension, Luxel<Dimension>, Environment<Dimension>>>()
                    }
                    val workers = List(threadCount) {
                        mock<SimulationWorker<Dimension, Environment<Dimension>>>()
                    }
                    val threadCountComputer = mock<ThreadCountComputer> {
                        every { getAvailableThreads(commonConfig) } returns threadCount
                    }
                    val filmProvider = mock<FilmProvider> {
                        every {
                            createFilm(commonConfig.outputFilmType, commonConfig.outputResolution)
                        } sequentiallyReturns films
                    }
                    val scene = mock<Scene<Dimension, Luxel<Dimension>, Long, Environment<Dimension>>> {
                        every { outputName() } returns ("foo")
                        every { initSimulator(commonConfig.animationFrameInfo) } sequentiallyReturns simulators
                    }
                    val workerProvider = mock<WorkerProvider> {
                        every {
                            createWorker(
                                any<Simulator<*, *, *>>(),
                                expectedWorkerConfig,
                            )
                        } sequentiallyReturns workers
                    }
                    val testedRunner = ParallelSimulationRunner(
                        logHandler,
                        workerProvider,
                        filmProvider,
                        threadCountComputer,
                    )

                    testedRunner.runSimulation(scene, sceneConfig, commonConfig)

                    repeat(threadCount) { idx ->
                        verifySuspend {
                            workers[idx].runSimulation(any(), sceneConfig, expectedWorkerConfig)
                        }
                    }
                }
            }
        }
    },
)
