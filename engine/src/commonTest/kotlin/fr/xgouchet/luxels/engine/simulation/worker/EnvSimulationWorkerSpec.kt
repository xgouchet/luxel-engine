package fr.xgouchet.luxels.engine.simulation.worker

import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verify.VerifyMode.Companion.not
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.render.Exposure
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.render.Projection
import fr.xgouchet.luxels.engine.simulation.SimulationContext
import fr.xgouchet.luxels.engine.test.kotest.property.colorArb
import fr.xgouchet.luxels.engine.test.kotest.property.commonConfigurationArb
import fr.xgouchet.luxels.engine.test.kotest.property.sceneConfigurationArb
import fr.xgouchet.luxels.engine.test.kotest.property.vectorArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class EnvSimulationWorkerSpec : DescribeSpec(
    {
        describe("simulateSingleLuxel") {
            it("spawns the expected number of luxels") {
                checkAll(
                    sceneConfigurationArb(),
                    commonConfigurationArb(),
                    Arb.int(min = 4, max = 128),
                    vectorArb(),
                    colorArb(),
                ) { sceneConfig, commonConfig, luxelCount, position, envColor ->
                    val luxels = List(luxelCount) {
                        mock<Luxel<Dimension>> {
                            every { isAlive() } returns false
                            every { position() } returns position
                        }
                    }
                    val environment = mock<Environment<Dimension>> {
                        every {
                            environmentColor(
                                any(),
                                commonConfig.animationFrameInfo.time,
                            )
                        } returns envColor
                    }
                    val exposure = mock<Exposure<Dimension>>()
                    val projection = mock<Projection<Dimension>>()
                    val logHandler = mock<LogHandler>()
                    val simulator = mock<Simulator<Dimension, Luxel<Dimension>, Environment<Dimension>>> {
                        every {
                            spawnLuxel(environment, commonConfig.animationFrameInfo)
                        } sequentiallyReturns luxels
                    }
                    val worker = EnvSimulationWorker(simulator, logHandler, 32)

                    worker.runSimulation(
                        exposure,
                        sceneConfig.copy(context = SimulationContext(environment, projection)),
                        commonConfig.copy(simulationLuxelCount = luxelCount.toLong()),
                    )

                    verify(not) { simulator.spawnLuxel(environment, commonConfig.animationFrameInfo) }
                    verify(exactly(luxelCount * 32)) { exposure.expose(any(), envColor) }
                }
            }
        }
    },
)
