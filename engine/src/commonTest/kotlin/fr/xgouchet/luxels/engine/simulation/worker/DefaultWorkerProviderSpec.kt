package fr.xgouchet.luxels.engine.simulation.worker

import dev.mokkery.mock
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.api.configuration.SimulationType
import fr.xgouchet.luxels.engine.test.kotest.property.commonConfigurationArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.property.checkAll

class DefaultWorkerProviderSpec : DescribeSpec(
    {
        describe("createWorker") {
            it("creates a Render worker") {
                checkAll(commonConfigurationArb()) { baseConfig ->
                    val configuration = baseConfig.copy(simulationType = SimulationType.RENDER)
                    val logHandler = mock<LogHandler>()
                    val simulator = mock<Simulator<Dimension, Luxel<Dimension>, Environment<Dimension>>>()

                    val worker = DefaultWorkerProvider(logHandler).createWorker(simulator, configuration)

                    worker.shouldBeTypeOf<RenderSimulationWorker<*, *, *>>()
                    val renderWorker = worker as RenderSimulationWorker<*, *, *>
                    renderWorker.simulator shouldBeSameInstanceAs simulator
                    renderWorker.logHandler shouldBeSameInstanceAs logHandler
                }
            }

            it("creates a Env worker") {
                checkAll(commonConfigurationArb()) { baseConfig ->
                    val configuration = baseConfig.copy(simulationType = SimulationType.ENV)
                    val logHandler = mock<LogHandler>()
                    val simulator = mock<Simulator<Dimension, Luxel<Dimension>, Environment<Dimension>>>()

                    val worker = DefaultWorkerProvider(logHandler).createWorker(simulator, configuration)

                    worker.shouldBeTypeOf<EnvSimulationWorker<*, *, *>>()
                    val renderWorker = worker as EnvSimulationWorker<*, *, *>
                    renderWorker.simulator shouldBeSameInstanceAs simulator
                    renderWorker.logHandler shouldBeSameInstanceAs logHandler
                }
            }

            it("creates a Spawn worker") {
                checkAll(commonConfigurationArb()) { baseConfig ->
                    val configuration = baseConfig.copy(simulationType = SimulationType.SPAWN)
                    val logHandler = mock<LogHandler>()
                    val simulator = mock<Simulator<Dimension, Luxel<Dimension>, Environment<Dimension>>>()

                    val worker = DefaultWorkerProvider(logHandler).createWorker(simulator, configuration)

                    worker.shouldBeTypeOf<SpawnSimulationWorker<*, *, *>>()
                    val renderWorker = worker as SpawnSimulationWorker<*, *, *>
                    renderWorker.simulator shouldBeSameInstanceAs simulator
                    renderWorker.logHandler shouldBeSameInstanceAs logHandler
                }
            }

            it("creates a Path worker") {
                checkAll(commonConfigurationArb()) { baseConfig ->
                    val configuration = baseConfig.copy(simulationType = SimulationType.PATH)
                    val logHandler = mock<LogHandler>()
                    val simulator = mock<Simulator<Dimension, Luxel<Dimension>, Environment<Dimension>>>()

                    val worker = DefaultWorkerProvider(logHandler).createWorker(simulator, configuration)

                    worker.shouldBeTypeOf<PathSimulationWorker<*, *, *>>()
                    val renderWorker = worker as PathSimulationWorker<*, *, *>
                    renderWorker.simulator shouldBeSameInstanceAs simulator
                    renderWorker.logHandler shouldBeSameInstanceAs logHandler
                }
            }

            it("creates a Death worker") {
                checkAll(commonConfigurationArb()) { baseConfig ->
                    val configuration = baseConfig.copy(simulationType = SimulationType.DEATH)
                    val logHandler = mock<LogHandler>()
                    val simulator = mock<Simulator<Dimension, Luxel<Dimension>, Environment<Dimension>>>()

                    val worker = DefaultWorkerProvider(logHandler).createWorker(simulator, configuration)

                    worker.shouldBeTypeOf<DeathSimulationWorker<*, *, *>>()
                    val renderWorker = worker as DeathSimulationWorker<*, *, *>
                    renderWorker.simulator shouldBeSameInstanceAs simulator
                    renderWorker.logHandler shouldBeSameInstanceAs logHandler
                }
            }
        }
    },
)
