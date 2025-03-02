package art.luxels.cli.demo.noise

import art.luxels.cli.common.AbstractSceneCommand
import art.luxels.components.engine.StaticLuxel
import art.luxels.core.math.Dimension.D2
import art.luxels.engine.api.Scene
import art.luxels.engine.api.input.InputSource

/**
 * [CliktCommand] for the [NoiseScene].
 */
class NoiseCommand : AbstractSceneCommand<D2, StaticLuxel<D2>, Unit, NoiseEnvironment>("noise", D2) {
    // region AbstractSceneCommand

    override fun getScene(): Scene<D2, StaticLuxel<D2>, Unit, NoiseEnvironment> {
        return NoiseScene()
    }

    override fun getInput(): InputSource<Unit> {
        return InputSource.Default
    }

    // endregion
}
