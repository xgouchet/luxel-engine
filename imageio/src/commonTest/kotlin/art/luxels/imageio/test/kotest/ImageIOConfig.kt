package art.luxels.imageio.test.kotest

import art.luxels.imageio.test.kotest.extensions.OkioExtension
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.property.PropertyTesting

open class ImageIOConfig : AbstractProjectConfig() {

    override val extensions: List<Extension> = super.extensions + OkioExtension()

    init {
        PropertyTesting.defaultIterationCount = 16
    }
}
