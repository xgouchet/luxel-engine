package fr.xgouchet.graphikio.test.kotest

import fr.xgouchet.graphikio.test.kotest.extensions.OkioExtension
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.property.PropertyTesting

open class GraphikIOConfig : AbstractProjectConfig() {
    init {
        PropertyTesting.defaultIterationCount = 16
    }

    override fun extensions(): List<Extension> {
        return super.extensions() + OkioExtension()
    }
}
