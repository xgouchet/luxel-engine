package art.luxels.engine.test.kotest

import io.kotest.assertions.AssertionsConfigSystemProperties
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.names.DuplicateTestNameMode
import io.kotest.property.PropertyTesting

class EngineJVMConfig : AbstractProjectConfig() {

    override val duplicateTestNameMode = DuplicateTestNameMode.Silent

    override val extensions: List<Extension> = listOf(MemoryGuardExtension())

    init {
        PropertyTesting.defaultIterationCount = 64
        System.setProperty(AssertionsConfigSystemProperties.disableNaNEquality, "true")

        println("JVM ${Runtime.version()} / Memory: ${Runtime.getRuntime().totalMemory()}b")
    }
}
