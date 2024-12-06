package fr.xgouchet.luxels.components.test.kotest

import io.kotest.assertions.AssertionsConfigSystemProperties
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.names.DuplicateTestNameMode
import io.kotest.property.PropertyTesting

class ComponentsJVMConfig : AbstractProjectConfig() {

    override val duplicateTestNameMode = DuplicateTestNameMode.Silent

    init {
        PropertyTesting.defaultIterationCount = 64
        System.setProperty(AssertionsConfigSystemProperties.disableNaNEquality, "true")

        println("JVM ${Runtime.version()} / Memory: ${Runtime.getRuntime().totalMemory()}b")
    }

    override fun extensions(): List<Extension> {
        return listOf(MemoryGuardExtension())
    }
}
