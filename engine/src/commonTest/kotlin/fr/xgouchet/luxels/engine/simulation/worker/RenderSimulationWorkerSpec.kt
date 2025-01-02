package fr.xgouchet.luxels.engine.simulation.worker

import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentially
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.exactly
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.render.Exposure
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.render.Projection
import fr.xgouchet.luxels.engine.simulation.SimulationContext
import fr.xgouchet.luxels.engine.test.kotest.property.colorArb
import fr.xgouchet.luxels.engine.test.kotest.property.internalConfigurationArb
import fr.xgouchet.luxels.engine.test.kotest.property.vectorArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class RenderSimulationWorkerSpec : DescribeSpec(
    {
        describe("runSimulation") {
            it("spawns, simulates and kills the expected number of luxels") {
                checkAll(
                    internalConfigurationArb(),
                    Arb.int(min = 4, max = 32),
                    Arb.int(min = 1, max = 8),
                    colorArb(),
                    vectorArb(),
                ) { baseConfig, luxelCount, lifespan, color, position ->
                    val luxels = List(luxelCount) {
                        mock<Luxel<Dimension>> {
                            every { isAlive() } sequentially {
                                repeat(lifespan) { returns(true) }
                                returns(false)
                            }
                            every { position() } returns position
                            every { color() } returns color
                        }
                    }
                    val environment = mock<Environment<Dimension>>()
                    val exposure = mock<Exposure<Dimension>>()
                    val projection = mock<Projection<Dimension>>()
                    val configuration = baseConfig.copy(
                        simulationLuxelCount = luxelCount.toLong(),
                        context = SimulationContext(environment, projection),
                    )
                    val logHandler = mock<LogHandler>()
                    val simulator = mock<Simulator<Dimension, Luxel<Dimension>, Environment<Dimension>>> {
                        every {
                            spawnLuxel(environment, configuration.animationFrameInfo)
                        } sequentiallyReturns luxels
                    }
                    val worker = RenderSimulationWorker(simulator, logHandler)

                    worker.runSimulation(exposure, configuration)

                    verify(exactly(luxelCount)) { simulator.spawnLuxel(environment, configuration.animationFrameInfo) }
                    repeat(luxelCount) { idx ->
                        verify { luxels[idx].onStart() }
                        verify(exactly(lifespan)) {
                            luxels[idx].onStep(any<Int>())
                            simulator.updateLuxel(luxels[idx], environment, configuration.animationFrameInfo)
                        }
                        verify { luxels[idx].onEnd() }
                    }
                    verify(exactly(luxelCount * lifespan)) { exposure.expose(position, color) }
                }
            }
        }
    },
)
