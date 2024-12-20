package fr.xgouchet.luxels.engine.simulation.runner

import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.asVolume
import fr.xgouchet.luxels.core.render.Film
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.render.DefaultFilmProvider
import fr.xgouchet.luxels.engine.render.FilmProvider
import fr.xgouchet.luxels.engine.render.Projection
import fr.xgouchet.luxels.engine.simulation.worker.DefaultWorkerProvider
import fr.xgouchet.luxels.engine.simulation.worker.SimulationWorker
import fr.xgouchet.luxels.engine.simulation.worker.WorkerProvider
import fr.xgouchet.luxels.engine.test.kotest.property.internalConfigurationArb
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
                    internalConfigurationArb(),
                    Arb.int(1, 3),
                ) { baseConfiguration, threadCount ->
                    val resolution = Resolution.SQUARE_128
                    val configuration = baseConfiguration.copy(outputResolution = resolution)
                    val environment = mock<Environment<Dimension>>()
                    val projection = mock<Projection<Dimension>>()
                    val logHandler = mock<LogHandler>()
                    val expectedWorkerConfig = configuration.copy(
                        simulationLuxelCount = configuration.simulationLuxelCount / threadCount,
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
                        every { getAvailableThreads(configuration) } returns threadCount
                    }
                    val filmProvider = mock<FilmProvider> {
                        every {
                            createFilm(configuration.outputFilmType, configuration.outputResolution)
                        } sequentiallyReturns films
                    }
                    val scene = mock<Scene<Dimension, Luxel<Dimension>, Long, Environment<Dimension>>> {
                        every { outputName() } returns ("foo")
                        every {
                            getFrameEnvironment(
                                configuration.animationFrameInfo,
                            )
                        } returns (environment)
                        every {
                            getProjection(
                                configuration.simulationVolume,
                                configuration.outputResolution.asVector2().asVolume(),
                                configuration.animationFrameInfo,
                            )
                        } returns projection
                        every { initSimulator(configuration.animationFrameInfo) } sequentiallyReturns simulators
                    }
                    val workerProvider = mock<WorkerProvider> {
                        every { createWorker(any(), expectedWorkerConfig) } sequentiallyReturns workers
                    }
                    val testedRunner = ParallelSimulationRunner(
                        logHandler,
                        workerProvider,
                        filmProvider,
                        threadCountComputer,
                    )

                    testedRunner.runSimulation(scene, configuration)

                    repeat(threadCount) { idx ->
                        verifySuspend {
                            workers[idx].runSimulation(environment, any(), expectedWorkerConfig)
                        }
                    }
                }
            }
        }
    },
)
