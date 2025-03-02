package art.luxels.engine.simulation.worker

import art.luxels.core.log.LogHandler
import art.luxels.core.math.Dimension
import art.luxels.core.render.Exposure
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Luxel
import art.luxels.engine.api.Simulator
import art.luxels.engine.render.Projection
import art.luxels.engine.simulation.SimulationContext
import art.luxels.engine.test.kotest.property.commonConfigurationArb
import art.luxels.engine.test.kotest.property.sceneConfigurationArb
import art.luxels.engine.test.kotest.property.vectorArb
import art.luxels.imageio.color.HDRColor
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.exactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class SpawnSimulationWorkerSpec : DescribeSpec(
    {
        describe("simulateSingleLuxel") {
            it("spawns the expected number of luxels") {
                checkAll(
                    sceneConfigurationArb(),
                    commonConfigurationArb(),
                    Arb.int(min = 4, max = 128),
                    vectorArb(),
                ) { sceneConfig, commonConfig, luxelCount, position ->
                    val luxels = List(luxelCount) {
                        mock<Luxel<Dimension>> {
                            every { isAlive() } returns false
                            every { position() } returns position
                        }
                    }
                    val environment = mock<Environment<Dimension>>()
                    val exposure = mock<Exposure<Dimension>>()
                    val projection = mock<Projection<Dimension>>()
                    val logHandler = mock<LogHandler>()
                    val simulator = mock<Simulator<Dimension, Luxel<Dimension>, Environment<Dimension>>> {
                        every {
                            spawnLuxel(environment, commonConfig.animationFrameInfo)
                        } sequentiallyReturns luxels
                    }
                    val worker = SpawnSimulationWorker(simulator, logHandler)

                    worker.runSimulation(
                        exposure,
                        sceneConfig.copy(context = SimulationContext(environment, projection)),
                        commonConfig.copy(simulationLuxelCount = luxelCount.toLong()),
                    )

                    verify(exactly(luxelCount)) { simulator.spawnLuxel(environment, commonConfig.animationFrameInfo) }
                    verify(exactly(luxelCount)) { exposure.expose(position, HDRColor.GREEN) }
                    repeat(luxelCount) { idx ->
                        verify { luxels[idx].onStart() }
                        verify(exactly(0)) { luxels[idx].onEnd() }
                    }
                }
            }
        }
    },
)
