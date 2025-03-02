package art.luxels.engine.simulation.runner

import art.luxels.core.concurrency.ConcurrencyCapabilities
import art.luxels.engine.test.kotest.property.commonConfigurationArb
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class DefaultThreadCountComputerSpec : DescribeSpec(
    {
        describe("getAvailableThreads") {
            it("is bound by cpu cores") {
                checkAll(
                    commonConfigurationArb(),
                    Arb.int(1, 32),
                    Arb.int(64, 128),
                    Arb.int(64, 128),
                ) { baseConfig, cpuThreads, memoryThreads, userThreads ->
                    val configuration = baseConfig.copy(simulationMaxThreadCount = userThreads)
                    val concurrencyCaps = mock<ConcurrencyCapabilities> {
                        every { getCpuParallelCapacity() } returns cpuThreads
                        every { getMemoryParallelCapacity(configuration.outputResolution) } returns memoryThreads
                    }
                    val computer = DefaultThreadCountComputer(concurrencyCaps)

                    val maxThreadCount = computer.getAvailableThreads(configuration)

                    maxThreadCount shouldBeExactly cpuThreads
                }
            }

            it("is bound by memory space") {
                checkAll(
                    commonConfigurationArb(),
                    Arb.int(64, 128),
                    Arb.int(1, 32),
                    Arb.int(64, 128),
                ) { baseConfig, cpuThreads, memoryThreads, userThreads ->
                    val configuration = baseConfig.copy(simulationMaxThreadCount = userThreads)
                    val concurrencyCaps = mock<ConcurrencyCapabilities> {
                        every { getCpuParallelCapacity() } returns cpuThreads
                        every { getMemoryParallelCapacity(configuration.outputResolution) } returns memoryThreads
                    }
                    val computer = DefaultThreadCountComputer(concurrencyCaps)

                    val maxThreadCount = computer.getAvailableThreads(configuration)

                    maxThreadCount shouldBeExactly memoryThreads
                }
            }

            it("is bound by user decision") {
                checkAll(
                    commonConfigurationArb(),
                    Arb.int(64, 128),
                    Arb.int(64, 128),
                    Arb.int(1, 32),
                ) { baseConfig, cpuThreads, memoryThreads, userThreads ->
                    val configuration = baseConfig.copy(simulationMaxThreadCount = userThreads)
                    val concurrencyCaps = mock<ConcurrencyCapabilities> {
                        every { getCpuParallelCapacity() } returns cpuThreads
                        every { getMemoryParallelCapacity(configuration.outputResolution) } returns memoryThreads
                    }
                    val computer = DefaultThreadCountComputer(concurrencyCaps)

                    val maxThreadCount = computer.getAvailableThreads(configuration)

                    maxThreadCount shouldBeExactly userThreads
                }
            }
        }
    },
)
